import 'dart:convert';
import 'dart:io';

import 'package:postgres/postgres.dart';

/// Acesso ao banco PostgreSQL do servidor.
///
/// Cada "mesa" (mesa de RPG) fica na tabela `mesas`; as fichas de todos os
/// jogadores de uma mesa ficam na tabela `fichas`, referenciando `mesa_id`.
/// O conteúdo da ficha é um JSON opaco guardado na coluna `dados` (JSONB).
class Db {
  final Pool _pool;
  Db(this._pool);

  /// Abre o pool a partir das variáveis de ambiente (com defaults do compose),
  /// espera o Postgres ficar pronto e garante que as tabelas existam.
  static Future<Db> connect() async {
    final endpoint = Endpoint(
      host: Platform.environment['DB_HOST'] ?? 'db',
      port: int.parse(Platform.environment['DB_PORT'] ?? '5432'),
      database: Platform.environment['DB_NAME'] ?? 'mesa',
      username: Platform.environment['DB_USER'] ?? 'mesa',
      password: Platform.environment['DB_PASSWORD'] ?? 'mesa',
    );
    final pool = Pool.withEndpoints(
      [endpoint],
      settings: const PoolSettings(
        maxConnectionCount: 5,
        sslMode: SslMode.disable,
      ),
    );

    // Postgres pode ainda estar subindo — tenta por até ~60s.
    Object? ultimoErro;
    for (var i = 0; i < 30; i++) {
      try {
        await pool.execute('SELECT 1');
        ultimoErro = null;
        break;
      } catch (e) {
        ultimoErro = e;
        stderr.writeln('Aguardando o banco... (${i + 1})');
        await Future<void>.delayed(const Duration(seconds: 2));
      }
    }
    if (ultimoErro != null) {
      throw StateError('Não foi possível conectar ao banco: $ultimoErro');
    }

    final db = Db(pool);
    await db._migrar();
    return db;
  }

  Future<void> _migrar() async {
    await _pool.execute('''
      CREATE TABLE IF NOT EXISTS mesas (
        id         UUID PRIMARY KEY,
        codigo     TEXT UNIQUE NOT NULL,
        nome       TEXT NOT NULL,
        created_at TIMESTAMPTZ NOT NULL DEFAULT now()
      );
    ''');
    await _pool.execute('''
      CREATE TABLE IF NOT EXISTS fichas (
        id              UUID PRIMARY KEY,
        mesa_id         UUID NOT NULL REFERENCES mesas(id) ON DELETE CASCADE,
        jogador_id      TEXT NOT NULL,
        nome_personagem TEXT NOT NULL,
        versao          INTEGER NOT NULL DEFAULT 1,
        dados           JSONB NOT NULL DEFAULT '{}'::jsonb,
        updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
        UNIQUE (mesa_id, jogador_id)
      );
    ''');
  }

  // ------------------------------------------------------------------ mesas

  /// Cria a mesa gerando um código único (tenta novamente se colidir).
  Future<Map<String, dynamic>> criarMesa({
    required String id,
    required String nome,
    required String Function() gerarCodigo,
  }) async {
    for (var tentativa = 0; tentativa < 10; tentativa++) {
      final codigo = gerarCodigo();
      try {
        final r = await _pool.execute(
          Sql.named(
            'INSERT INTO mesas (id, codigo, nome) '
            'VALUES (@id::uuid, @codigo, @nome) '
            'RETURNING id::text AS id, codigo, nome, created_at::text AS created_at',
          ),
          parameters: {'id': id, 'codigo': codigo, 'nome': nome},
        );
        return r.first.toColumnMap();
      } catch (e) {
        // 23505 = unique_violation (código repetido) → tenta outro código.
        if (e.toString().contains('23505') ||
            e.toString().contains('duplicate key')) {
          continue;
        }
        rethrow;
      }
    }
    throw StateError('Não foi possível gerar um código de mesa único');
  }

  Future<List<Map<String, dynamic>>> listarMesas() async {
    final r = await _pool.execute(
      'SELECT id::text AS id, codigo, nome, created_at::text AS created_at '
      'FROM mesas ORDER BY created_at DESC',
    );
    return r.map((row) => row.toColumnMap()).toList();
  }

  Future<Map<String, dynamic>?> buscarMesaPorId(String id) async {
    final r = await _pool.execute(
      Sql.named(
        'SELECT id::text AS id, codigo, nome, created_at::text AS created_at '
        'FROM mesas WHERE id = @id::uuid',
      ),
      parameters: {'id': id},
    );
    return r.isEmpty ? null : r.first.toColumnMap();
  }

  Future<Map<String, dynamic>?> buscarMesaPorCodigo(String codigo) async {
    final r = await _pool.execute(
      Sql.named(
        'SELECT id::text AS id, codigo, nome, created_at::text AS created_at '
        'FROM mesas WHERE codigo = @codigo',
      ),
      parameters: {'codigo': codigo},
    );
    return r.isEmpty ? null : r.first.toColumnMap();
  }

  Future<bool> removerMesa(String id) async {
    final r = await _pool.execute(
      Sql.named('DELETE FROM mesas WHERE id = @id::uuid'),
      parameters: {'id': id},
    );
    return r.affectedRows > 0;
  }

  // ----------------------------------------------------------------- fichas

  Future<List<Map<String, dynamic>>> listarFichas({
    required String mesaId,
    String? jogadorId,
  }) async {
    final filtroJogador = jogadorId != null ? 'AND jogador_id = @jog' : '';
    final r = await _pool.execute(
      Sql.named(
        'SELECT id::text AS id, mesa_id::text AS mesa_id, jogador_id, '
        'nome_personagem, versao, dados::text AS dados, '
        'updated_at::text AS updated_at '
        'FROM fichas WHERE mesa_id = @mesa::uuid $filtroJogador '
        'ORDER BY nome_personagem',
      ),
      parameters: {'mesa': mesaId, if (jogadorId != null) 'jog': jogadorId},
    );
    return r.map((row) => _ficha(row.toColumnMap())).toList();
  }

  Future<Map<String, dynamic>?> buscarFicha({
    required String mesaId,
    required String jogadorId,
  }) async {
    final r = await _pool.execute(
      Sql.named(
        'SELECT id::text AS id, mesa_id::text AS mesa_id, jogador_id, '
        'nome_personagem, versao, dados::text AS dados, '
        'updated_at::text AS updated_at '
        'FROM fichas WHERE mesa_id = @mesa::uuid AND jogador_id = @jog',
      ),
      parameters: {'mesa': mesaId, 'jog': jogadorId},
    );
    return r.isEmpty ? null : _ficha(r.first.toColumnMap());
  }

  /// Sincroniza a ficha do jogador na mesa seguindo a regra "maior versão vence":
  /// - não existe no servidor → cria (status `criada`);
  /// - versão recebida > versão no servidor → atualiza (status `atualizada`);
  /// - versão recebida <= servidor → nada a fazer (status `inalterada`).
  Future<Map<String, dynamic>> sincronizarFicha({
    required String mesaId,
    required String jogadorId,
    required String nomePersonagem,
    required int versao,
    required String dadosJson,
    required String novoId,
  }) async {
    final existente = await buscarFicha(mesaId: mesaId, jogadorId: jogadorId);

    if (existente == null) {
      final r = await _pool.execute(
        Sql.named(
          'INSERT INTO fichas (id, mesa_id, jogador_id, nome_personagem, versao, dados) '
          'VALUES (@id::uuid, @mesa::uuid, @jog, @nome, @versao, @dados::jsonb) '
          'RETURNING id::text AS id, mesa_id::text AS mesa_id, jogador_id, '
          'nome_personagem, versao, dados::text AS dados, updated_at::text AS updated_at',
        ),
        parameters: {
          'id': novoId,
          'mesa': mesaId,
          'jog': jogadorId,
          'nome': nomePersonagem,
          'versao': versao,
          'dados': dadosJson,
        },
      );
      return {'status': 'criada', 'ficha': _ficha(r.first.toColumnMap())};
    }

    final versaoServidor = existente['versao'] as int;
    if (versao > versaoServidor) {
      final r = await _pool.execute(
        Sql.named(
          'UPDATE fichas SET nome_personagem = @nome, versao = @versao, '
          'dados = @dados::jsonb, updated_at = now() '
          'WHERE mesa_id = @mesa::uuid AND jogador_id = @jog '
          'RETURNING id::text AS id, mesa_id::text AS mesa_id, jogador_id, '
          'nome_personagem, versao, dados::text AS dados, updated_at::text AS updated_at',
        ),
        parameters: {
          'mesa': mesaId,
          'jog': jogadorId,
          'nome': nomePersonagem,
          'versao': versao,
          'dados': dadosJson,
        },
      );
      return {'status': 'atualizada', 'ficha': _ficha(r.first.toColumnMap())};
    }

    return {
      'status': 'inalterada',
      'versao_servidor': versaoServidor,
      'ficha': existente,
    };
  }

  /// Decodifica a coluna `dados` (que vem como texto JSON) para objeto.
  Map<String, dynamic> _ficha(Map<String, dynamic> row) {
    final dados = row['dados'];
    if (dados is String) {
      row['dados'] = jsonDecode(dados);
    }
    return row;
  }
}

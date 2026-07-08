import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:sqflite/sqflite.dart';
import 'package:uuid/uuid.dart';

import '../local/database_helper.dart';
import '../models/ficha_model.dart';
import '../models/mesa_model.dart';

/// Resultado de uma tentativa de sincronização com o servidor.
class ResultadoSync {
  final bool sincronizada;
  final String mensagem;
  const ResultadoSync(this.sincronizada, this.mensagem);
}

/// Ficha: fonte da verdade é o celular (sqflite). Salva sempre local primeiro
/// (incrementando a versão) e depois tenta enviar ao servidor.
class FichaRepository {
  final DatabaseHelper _dbHelper;
  final http.Client _client;

  FichaRepository(this._dbHelper, {http.Client? client})
      : _client = client ?? http.Client();

  /// Ficha da mesa (uma por mesa) — ou null se ainda não existe.
  Future<FichaModel?> fichaDaMesa(String mesaId) async {
    final db = await _dbHelper.database;
    final linhas = await db.query(
      'ficha_local',
      where: 'mesa_id = ?',
      whereArgs: [mesaId],
      limit: 1,
    );
    return linhas.isEmpty ? null : FichaModel.fromMap(linhas.first);
  }

  /// Salva localmente: cria (versão 1) ou atualiza (versão + 1), marcando
  /// `sincronizada = 0` (pendente de envio).
  Future<FichaModel> salvarLocal({
    required String mesaId,
    required String jogadorId,
    required String nomePersonagem,
    required String textoLivre,
  }) async {
    final db = await _dbHelper.database;
    final atual = await fichaDaMesa(mesaId);

    final ficha = FichaModel(
      id: atual?.id ?? const Uuid().v4(),
      mesaId: mesaId,
      jogadorId: jogadorId,
      nomePersonagem: nomePersonagem,
      dadosJson: jsonEncode({'conteudo': textoLivre}),
      versao: (atual?.versao ?? 0) + 1,
      sincronizada: false,
      updatedAt: DateTime.now().toIso8601String(),
    );

    await db.insert(
      'ficha_local',
      ficha.toMap(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
    return ficha;
  }

  /// Envia a ficha ao servidor. O servidor decide ("maior versão vence"):
  /// se não tiver essa versão, grava. Em caso de sucesso, marca sincronizada=1.
  Future<ResultadoSync> sincronizar(MesaModel mesa, FichaModel ficha) async {
    try {
      final res = await _client
          .post(
            Uri.parse('${mesa.ipServidor}/mesa/fichas'),
            headers: {
              'X-Mesa-Codigo': mesa.codigo,
              'Content-Type': 'application/json',
            },
            body: jsonEncode(ficha.paraEnvio()),
          )
          .timeout(const Duration(seconds: 10));

      if (res.statusCode == 200 || res.statusCode == 201) {
        await _marcarSincronizada(ficha.id);
        final status =
            jsonDecode(utf8.decode(res.bodyBytes))['status'] ?? 'ok';
        return ResultadoSync(true, 'Sincronizada com o servidor ($status).');
      }
      return ResultadoSync(
        false,
        'Servidor recusou (HTTP ${res.statusCode}). Ficha salva no celular.',
      );
    } catch (e) {
      return ResultadoSync(
        false,
        'Sem conexão com o servidor. Ficha salva no celular.',
      );
    }
  }

  Future<void> _marcarSincronizada(String fichaId) async {
    final db = await _dbHelper.database;
    await db.update(
      'ficha_local',
      {'sincronizada': 1},
      where: 'id = ?',
      whereArgs: [fichaId],
    );
  }
}

import 'dart:convert';
import 'dart:math';

import 'package:shelf/shelf.dart';
import 'package:shelf_router/shelf_router.dart';
import 'package:uuid/uuid.dart';

import 'db.dart';
import 'middleware.dart';

const _uuid = Uuid();

/// Monta todas as rotas da API.
///
/// Rotas do MESTRE (usadas pelo navegador em localhost, sem código):
///   POST   /mesas                  cria mesa (gera código + id)
///   GET    /mesas                  lista mesas
///   GET    /mesas/<id>             detalhe da mesa
///   GET    /mesas/<id>/fichas      todas as fichas da mesa (visão do mestre)
///   DELETE /mesas/<id>             remove a mesa
///
/// Rotas do JOGADOR (APK) — exigem o header `X-Mesa-Codigo`:
///   GET    /mesa/info              entra/valida e retorna a mesa do código
///   GET    /mesa/fichas            fichas da mesa (aceita ?jogador_id=)
///   GET    /mesa/fichas/<jogId>    ficha de um jogador (checagem de versão)
///   POST   /mesa/fichas            cria/sincroniza ficha ("maior versão vence")
Handler buildRouter(Db db) {
  final router = Router();

  router.get('/', (Request req) {
    return _json(200, {
      'app': 'Mesa D&D — API',
      'status': 'ok',
      'rotas_mestre': ['POST /mesas', 'GET /mesas', 'GET /mesas/<id>/fichas'],
      'rotas_jogador': [
        'GET /mesa/info',
        'GET /mesa/fichas',
        'POST /mesa/fichas'
      ],
    });
  });

  // ---------------------------------------------------------------- mestre
  router.post('/mesas', (Request req) async {
    final body = await _body(req);
    final nome = (body['nome'] as String?)?.trim();
    if (nome == null || nome.isEmpty) {
      return _json(400, {'erro': 'Campo "nome" é obrigatório'});
    }
    final mesa = await db.criarMesa(
      id: _uuid.v4(),
      nome: nome,
      gerarCodigo: _gerarCodigo,
    );
    return _json(201, mesa);
  });

  router.get('/mesas', (Request req) async {
    return _json(200, await db.listarMesas());
  });

  router.get('/mesas/<id>', (Request req, String id) async {
    final mesa = await db.buscarMesaPorId(id);
    return mesa == null
        ? _json(404, {'erro': 'Mesa não encontrada'})
        : _json(200, mesa);
  });

  router.get('/mesas/<id>/fichas', (Request req, String id) async {
    final mesa = await db.buscarMesaPorId(id);
    if (mesa == null) return _json(404, {'erro': 'Mesa não encontrada'});
    return _json(200, await db.listarFichas(mesaId: id));
  });

  router.delete('/mesas/<id>', (Request req, String id) async {
    final removida = await db.removerMesa(id);
    return removida
        ? _json(200, {'status': 'removida'})
        : _json(404, {'erro': 'Mesa não encontrada'});
  });

  // --------------------------------------------------------------- jogador
  final jogador = Router();

  jogador.get('/info', (Request req) {
    return _json(200, _mesaDoContexto(req));
  });

  jogador.get('/fichas', (Request req) async {
    final mesa = _mesaDoContexto(req);
    final jogadorId = req.url.queryParameters['jogador_id'];
    final fichas = await db.listarFichas(
      mesaId: mesa['id'] as String,
      jogadorId: jogadorId,
    );
    return _json(200, fichas);
  });

  jogador.get('/fichas/<jogadorId>', (Request req, String jogadorId) async {
    final mesa = _mesaDoContexto(req);
    final ficha = await db.buscarFicha(
      mesaId: mesa['id'] as String,
      jogadorId: jogadorId,
    );
    return ficha == null
        ? _json(404, {'erro': 'Ficha não encontrada'})
        : _json(200, ficha);
  });

  jogador.post('/fichas', (Request req) async {
    final mesa = _mesaDoContexto(req);
    final body = await _body(req);

    final jogadorId = (body['jogador_id'] as String?)?.trim();
    final nome = (body['nome_personagem'] as String?)?.trim();
    final versao = body['versao'];
    final dados = body['dados'];
    if (jogadorId == null ||
        jogadorId.isEmpty ||
        nome == null ||
        nome.isEmpty ||
        versao is! int ||
        dados == null) {
      return _json(400, {
        'erro': 'Campos obrigatórios: jogador_id, nome_personagem, '
            'versao (inteiro), dados',
      });
    }

    final resultado = await db.sincronizarFicha(
      mesaId: mesa['id'] as String,
      jogadorId: jogadorId,
      nomePersonagem: nome,
      versao: versao,
      dadosJson: jsonEncode(dados),
      novoId: _uuid.v4(),
    );
    final status = resultado['status'];
    return _json(status == 'criada' ? 201 : 200, resultado);
  });

  // As rotas do jogador ficam atrás do middleware que exige o código no header.
  router.mount(
    '/mesa',
    Pipeline().addMiddleware(exigirCodigoMesa(db)).addHandler(jogador.call),
  );

  return router.call;
}

Map<String, dynamic> _mesaDoContexto(Request req) =>
    req.context['mesa'] as Map<String, dynamic>;

Future<Map<String, dynamic>> _body(Request req) async {
  final texto = await req.readAsString();
  if (texto.isEmpty) return {};
  final decodificado = jsonDecode(texto);
  return decodificado is Map<String, dynamic> ? decodificado : {};
}

/// Código da mesa: 6 caracteres alfanuméricos maiúsculos (ex.: "A7K2QP").
String _gerarCodigo() {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  final rnd = Random.secure();
  return List.generate(6, (_) => chars[rnd.nextInt(chars.length)]).join();
}

Response _json(int status, Object? body) => Response(
      status,
      body: jsonEncode(body),
      headers: {'Content-Type': 'application/json; charset=utf-8'},
    );

import 'dart:convert';

import 'package:shelf/shelf.dart';

import 'db.dart';

const _corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
  'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Mesa-Codigo',
};

/// Converte exceções em respostas JSON: corpo malformado (JSON/UTF-8 inválido)
/// vira 400; qualquer outra falha vira 500 — nunca uma resposta vazia.
Middleware tratarErros() {
  return (Handler inner) {
    return (Request req) async {
      try {
        return await inner(req);
      } on FormatException catch (e) {
        return _erro(400, 'Corpo inválido (JSON/UTF-8): ${e.message}');
      } catch (e) {
        return _erro(500, 'Erro interno: $e');
      }
    };
  };
}

/// Libera o acesso do navegador do mestre (origem diferente da API) e responde
/// os preflights OPTIONS.
Middleware cors() {
  return (Handler inner) {
    return (Request req) async {
      if (req.method == 'OPTIONS') {
        return Response.ok(null, headers: _corsHeaders);
      }
      final res = await inner(req);
      return res.change(headers: _corsHeaders);
    };
  };
}

/// "Autenticação" do jogador: exige o header `X-Mesa-Codigo` com o código da
/// mesa (o mesmo que o mestre mostra na tela). Resolve a mesa e coloca em
/// `request.context['mesa']` para os handlers usarem. Testável no Postman.
Middleware exigirCodigoMesa(Db db) {
  return (Handler inner) {
    return (Request req) async {
      final codigo = req.headers['x-mesa-codigo']?.trim();
      if (codigo == null || codigo.isEmpty) {
        return _erro(401, 'Header X-Mesa-Codigo ausente');
      }
      final mesa = await db.buscarMesaPorCodigo(codigo);
      if (mesa == null) {
        return _erro(403, 'Código de mesa inválido');
      }
      return inner(req.change(context: {'mesa': mesa}));
    };
  };
}

Response _erro(int status, String mensagem) => Response(
      status,
      body: jsonEncode({'erro': mensagem}),
      headers: {'Content-Type': 'application/json'},
    );

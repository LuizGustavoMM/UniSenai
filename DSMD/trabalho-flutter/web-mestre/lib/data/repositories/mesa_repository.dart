import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import '../api_config.dart';
import '../models/ficha_model.dart';
import '../models/mesa_model.dart';

/// Fala com a API do servidor (HTTP + JSON) e mantém as mesas do mestre no
/// cache do navegador (localStorage, via shared_preferences).
///
/// Como não há login, o cache é a "memória" do mestre: as mesas que ele criou
/// ficam salvas aqui e aparecem em "Minhas mesas" mesmo depois de recarregar.
class MesaRepository {
  MesaRepository({http.Client? client}) : _client = client ?? http.Client();

  final http.Client _client;

  static const _cacheKey = 'mesas_do_mestre';

  /// Cria a mesa no servidor (que gera o código + id) e guarda no cache local.
  Future<MesaModel> criarMesa(String nome) async {
    final res = await _client.post(
      Uri.parse('$apiBaseUrl/mesas'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'nome': nome}),
    );
    if (res.statusCode != 201) {
      throw Exception('Falha ao criar mesa (HTTP ${res.statusCode})');
    }
    final mesa = MesaModel.fromJson(
      jsonDecode(utf8.decode(res.bodyBytes)) as Map<String, dynamic>,
    );
    await _salvarNoCache(mesa);
    return mesa;
  }

  /// Lê as mesas guardadas no navegador.
  Future<List<MesaModel>> mesasDoCache() async {
    final prefs = await SharedPreferences.getInstance();
    final raw = prefs.getString(_cacheKey);
    if (raw == null || raw.isEmpty) return [];
    final lista = jsonDecode(raw) as List;
    return lista
        .map((e) => MesaModel.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  /// Remove a mesa do servidor e do cache.
  Future<void> removerMesa(String id) async {
    await _client.delete(Uri.parse('$apiBaseUrl/mesas/$id'));
    final prefs = await SharedPreferences.getInstance();
    final atuais = await mesasDoCache()
      ..removeWhere((m) => m.id == id);
    await prefs.setString(
      _cacheKey,
      jsonEncode(atuais.map((m) => m.toJson()).toList()),
    );
  }

  /// Busca no servidor todas as fichas de uma mesa (visão do mestre).
  Future<List<FichaModel>> fichasDaMesa(String mesaId) async {
    final res = await _client.get(Uri.parse('$apiBaseUrl/mesas/$mesaId/fichas'));
    if (res.statusCode != 200) {
      throw Exception('Falha ao buscar fichas (HTTP ${res.statusCode})');
    }
    final lista = jsonDecode(utf8.decode(res.bodyBytes)) as List;
    return lista
        .map((e) => FichaModel.fromJson(e as Map<String, dynamic>))
        .toList();
  }

  Future<void> _salvarNoCache(MesaModel mesa) async {
    final prefs = await SharedPreferences.getInstance();
    final atuais = await mesasDoCache()
      ..removeWhere((m) => m.id == mesa.id);
    atuais.insert(0, mesa);
    await prefs.setString(
      _cacheKey,
      jsonEncode(atuais.map((m) => m.toJson()).toList()),
    );
  }
}

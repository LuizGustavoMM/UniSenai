import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:sqflite/sqflite.dart';

import '../local/database_helper.dart';
import '../models/mesa_model.dart';

/// Entra em mesas (valida o código no servidor) e guarda as mesas no sqflite.
class MesaRepository {
  final DatabaseHelper _dbHelper;
  final http.Client _client;

  MesaRepository(this._dbHelper, {http.Client? client})
      : _client = client ?? http.Client();

  /// Normaliza o endereço digitado ("192.168.137.1:8000" → "http://192.168.137.1:8000").
  static String normalizarIp(String ip) {
    var t = ip.trim();
    if (!t.startsWith('http://') && !t.startsWith('https://')) {
      t = 'http://$t';
    }
    while (t.endsWith('/')) {
      t = t.substring(0, t.length - 1);
    }
    return t;
  }

  /// Valida o código no servidor (GET /mesa/info com header X-Mesa-Codigo) e,
  /// em caso de sucesso, salva a mesa localmente.
  Future<MesaModel> entrar({required String ip, required String codigo}) async {
    final base = normalizarIp(ip);
    final code = codigo.trim().toUpperCase();

    final res = await _client.get(
      Uri.parse('$base/mesa/info'),
      headers: {'X-Mesa-Codigo': code},
    ).timeout(const Duration(seconds: 10));

    if (res.statusCode == 401 || res.statusCode == 403) {
      throw Exception('Código de mesa inválido.');
    }
    if (res.statusCode != 200) {
      throw Exception('Falha ao entrar na mesa (HTTP ${res.statusCode}).');
    }

    final mesa = MesaModel.fromServidor(
      jsonDecode(utf8.decode(res.bodyBytes)) as Map<String, dynamic>,
      ipServidor: base,
    );

    final db = await _dbHelper.database;
    await db.insert(
      'mesa_local',
      mesa.toMap()..['joined_at'] = DateTime.now().toIso8601String(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
    return mesa;
  }

  Future<List<MesaModel>> mesasLocais() async {
    final db = await _dbHelper.database;
    final linhas = await db.query('mesa_local', orderBy: 'joined_at DESC');
    return linhas.map(MesaModel.fromMap).toList();
  }
}

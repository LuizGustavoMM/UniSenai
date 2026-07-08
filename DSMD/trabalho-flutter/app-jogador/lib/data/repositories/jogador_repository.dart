import 'package:uuid/uuid.dart';

import '../local/database_helper.dart';
import '../models/jogador_model.dart';

/// Identidade local do jogador (sem login): gera/lê o UUID + apelido no sqflite.
class JogadorRepository {
  final DatabaseHelper _dbHelper;
  JogadorRepository(this._dbHelper);

  /// Retorna o jogador salvo (ou null na primeira execução).
  Future<JogadorModel?> carregar() async {
    final db = await _dbHelper.database;
    final linhas = await db.query('jogador_local', limit: 1);
    return linhas.isEmpty ? null : JogadorModel.fromMap(linhas.first);
  }

  /// Cria a identidade na primeira execução: UUID gerado no app + apelido.
  Future<JogadorModel> criar(String apelido) async {
    final db = await _dbHelper.database;
    final jogador = JogadorModel(id: const Uuid().v4(), apelido: apelido);
    await db.insert('jogador_local', jogador.toMap());
    return jogador;
  }
}

import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

/// Banco local do celular (sqflite), no padrão singleton da aula "codigo sqlite".
///
/// Guarda só o necessário no dispositivo do jogador:
/// - `jogador_local`: identidade local (UUID + apelido), sem login;
/// - `mesa_local`: as mesas em que o jogador entrou (para reconectar);
/// - `ficha_local`: uma ficha por mesa, versionada, com flag de sincronização.
class DatabaseHelper {
  DatabaseHelper._();
  static final DatabaseHelper instance = DatabaseHelper._();

  static Database? _db;

  Future<Database> get database async => _db ??= await _abrir();

  Future<Database> _abrir() async {
    final caminho = join(await getDatabasesPath(), 'mesa_dnd_jogador.db');
    return openDatabase(caminho, version: 1, onCreate: _criar);
  }

  Future<void> _criar(Database db, int versao) async {
    await db.execute('''
      CREATE TABLE jogador_local (
        id      TEXT PRIMARY KEY,
        apelido TEXT NOT NULL
      )
    ''');
    await db.execute('''
      CREATE TABLE mesa_local (
        id          TEXT PRIMARY KEY,
        codigo      TEXT NOT NULL,
        nome        TEXT NOT NULL,
        ip_servidor TEXT NOT NULL,
        joined_at   TEXT NOT NULL
      )
    ''');
    await db.execute('''
      CREATE TABLE ficha_local (
        id              TEXT PRIMARY KEY,
        mesa_id         TEXT NOT NULL,
        jogador_id      TEXT NOT NULL,
        nome_personagem TEXT NOT NULL,
        dados_json      TEXT NOT NULL,
        versao          INTEGER NOT NULL,
        sincronizada    INTEGER NOT NULL DEFAULT 0,
        updated_at      TEXT NOT NULL,
        UNIQUE (mesa_id)
      )
    ''');
  }
}

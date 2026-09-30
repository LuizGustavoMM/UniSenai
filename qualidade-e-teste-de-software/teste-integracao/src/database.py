"""
Módulo de banco de dados da AeroLog Helicópteros.

Concentra a conexão com o SQLite e todas as operações sobre as tabelas de
aeronaves, pilotos e agendamentos. Nenhum outro módulo do sistema fala com o
banco diretamente: tanto o módulo de validação quanto o serviço de
agendamento passam por aqui.

O controle de transação é exposto de propósito. O serviço de agendamento
precisa garantir que, quando uma regra de negócio reprova o voo, nada fique
gravado na base.
"""

import sqlite3

# Status possíveis de manutenção de uma aeronave
MANUTENCAO_LIBERADO = "LIBERADO"
MANUTENCAO_EM_INSPECAO = "EM_INSPECAO"

# Status possíveis de um agendamento
AGENDAMENTO_AGENDADO = "AGENDADO"
AGENDAMENTO_CANCELADO = "CANCELADO"


ESQUEMA = """
CREATE TABLE IF NOT EXISTS aeronaves (
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    prefixo            TEXT    NOT NULL UNIQUE,
    modelo             TEXT    NOT NULL,
    status_manutencao  TEXT    NOT NULL,
    capacidade_kg      INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS pilotos (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome     TEXT NOT NULL,
    licenca  TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS agendamentos (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    aeronave_id    INTEGER NOT NULL,
    piloto_id      INTEGER NOT NULL,
    origem         TEXT    NOT NULL,
    destino        TEXT    NOT NULL,
    inicio         TEXT    NOT NULL,
    fim            TEXT    NOT NULL,
    peso_total_kg  INTEGER NOT NULL,
    status         TEXT    NOT NULL,
    FOREIGN KEY (aeronave_id) REFERENCES aeronaves (id),
    FOREIGN KEY (piloto_id)   REFERENCES pilotos (id)
);
"""


class Database:
    """Conexão e operações sobre o banco SQLite da operação."""

    def __init__(self, caminho=":memory:"):
        self.caminho = caminho
        self.conexao = None

    # ------------------------------------------------------- ciclo de vida ---

    def conectar(self):
        """Abre a conexão. O isolation_level nulo deixa a transação na mão
        do serviço de agendamento, que é quem precisa controlar o rollback."""
        self.conexao = sqlite3.connect(self.caminho, isolation_level=None)
        self.conexao.row_factory = sqlite3.Row
        self.conexao.execute("PRAGMA foreign_keys = ON")
        return self.conexao

    def criar_tabelas(self):
        self._exigir_conexao()
        self.conexao.executescript(ESQUEMA)

    def fechar(self):
        if self.conexao is not None:
            self.conexao.close()
            self.conexao = None

    def _exigir_conexao(self):
        if self.conexao is None:
            raise RuntimeError("Banco não conectado. Chame conectar() antes.")

    # ---------------------------------------------------------- transações ---

    def iniciar_transacao(self):
        self._exigir_conexao()
        self.conexao.execute("BEGIN")

    def confirmar(self):
        self._exigir_conexao()
        self.conexao.execute("COMMIT")

    def desfazer(self):
        self._exigir_conexao()
        self.conexao.execute("ROLLBACK")

    # ------------------------------------------------------------ aeronaves ---

    def inserir_aeronave(self, prefixo, modelo, status_manutencao, capacidade_kg):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "INSERT INTO aeronaves (prefixo, modelo, status_manutencao, capacidade_kg) "
            "VALUES (?, ?, ?, ?)",
            (prefixo, modelo, status_manutencao, capacidade_kg),
        )
        return cursor.lastrowid

    def buscar_aeronave_por_prefixo(self, prefixo):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "SELECT * FROM aeronaves WHERE prefixo = ?", (prefixo,)
        )
        return cursor.fetchone()

    def atualizar_status_manutencao(self, prefixo, novo_status):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "UPDATE aeronaves SET status_manutencao = ? WHERE prefixo = ?",
            (novo_status, prefixo),
        )
        return cursor.rowcount

    # -------------------------------------------------------------- pilotos ---

    def inserir_piloto(self, nome, licenca):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "INSERT INTO pilotos (nome, licenca) VALUES (?, ?)", (nome, licenca)
        )
        return cursor.lastrowid

    def buscar_piloto(self, piloto_id):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "SELECT * FROM pilotos WHERE id = ?", (piloto_id,)
        )
        return cursor.fetchone()

    # --------------------------------------------------------- agendamentos ---

    def inserir_agendamento(self, aeronave_id, piloto_id, origem, destino,
                            inicio, fim, peso_total_kg, status):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "INSERT INTO agendamentos "
            "(aeronave_id, piloto_id, origem, destino, inicio, fim, peso_total_kg, status) "
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            (aeronave_id, piloto_id, origem, destino, inicio, fim, peso_total_kg, status),
        )
        return cursor.lastrowid

    def buscar_agendamento(self, agendamento_id):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "SELECT * FROM agendamentos WHERE id = ?", (agendamento_id,)
        )
        return cursor.fetchone()

    def listar_agendamentos(self, aeronave_id=None):
        self._exigir_conexao()
        if aeronave_id is None:
            cursor = self.conexao.execute(
                "SELECT * FROM agendamentos ORDER BY inicio"
            )
        else:
            cursor = self.conexao.execute(
                "SELECT * FROM agendamentos WHERE aeronave_id = ? ORDER BY inicio",
                (aeronave_id,),
            )
        return cursor.fetchall()

    def buscar_conflitos_de_horario(self, aeronave_id, inicio, fim):
        """Agendamentos ativos da aeronave que se sobrepõem ao período.

        Dois períodos se sobrepõem quando um começa antes do outro terminar e
        termina depois do outro começar. Voos encostados, em que um termina
        exatamente quando o outro começa, não são conflito.
        """
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "SELECT * FROM agendamentos "
            "WHERE aeronave_id = ? AND status = ? AND inicio < ? AND fim > ?",
            (aeronave_id, AGENDAMENTO_AGENDADO, fim, inicio),
        )
        return cursor.fetchall()

    def contar_agendamentos(self):
        self._exigir_conexao()
        cursor = self.conexao.execute("SELECT COUNT(*) AS total FROM agendamentos")
        return cursor.fetchone()["total"]

    def cancelar_agendamento(self, agendamento_id):
        self._exigir_conexao()
        cursor = self.conexao.execute(
            "UPDATE agendamentos SET status = ? WHERE id = ?",
            (AGENDAMENTO_CANCELADO, agendamento_id),
        )
        return cursor.rowcount

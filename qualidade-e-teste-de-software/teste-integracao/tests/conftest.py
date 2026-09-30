"""
Fixtures do pytest para os testes de integração.

Cada teste recebe um banco SQLite novo, criado em um arquivo temporário
próprio daquele teste. O pytest destrói o diretório ao final, então um teste
nunca enxerga o que outro gravou. É esse isolamento que o Cenário 4 do
trabalho verifica.

Optou-se por arquivo em disco, e não por banco em memória, justamente para o
teste exercitar o caminho real de persistência, com abertura de conexão,
commit e rollback sobre um arquivo de verdade.
"""

import pytest

from src.booking_service import BookingService
from src.database import (
    MANUTENCAO_EM_INSPECAO,
    MANUTENCAO_LIBERADO,
    Database,
)
from src.validation import ValidationModule

from dados_frota import (
    CAPACIDADE_EM_INSPECAO,
    CAPACIDADE_LIBERADO,
    CAPACIDADE_POUCA,
    PILOTO_PRINCIPAL,
    PILOTO_SECUNDARIO,
    PREFIXO_EM_INSPECAO,
    PREFIXO_LIBERADO,
    PREFIXO_POUCA_CAPACIDADE,
)


@pytest.fixture
def caminho_banco(tmp_path):
    """Caminho de um arquivo SQLite exclusivo deste teste."""
    return str(tmp_path / "aerolog_teste.db")


@pytest.fixture
def banco(caminho_banco):
    """Banco conectado, com as tabelas criadas e a frota de teste cadastrada."""
    db = Database(caminho_banco)
    db.conectar()
    db.criar_tabelas()

    db.inserir_aeronave(
        PREFIXO_LIBERADO, "Airbus H125", MANUTENCAO_LIBERADO, CAPACIDADE_LIBERADO
    )
    db.inserir_aeronave(
        PREFIXO_EM_INSPECAO, "Bell 407", MANUTENCAO_EM_INSPECAO, CAPACIDADE_EM_INSPECAO
    )
    db.inserir_aeronave(
        PREFIXO_POUCA_CAPACIDADE, "Robinson R44", MANUTENCAO_LIBERADO, CAPACIDADE_POUCA
    )
    db.inserir_piloto(*PILOTO_PRINCIPAL)
    db.inserir_piloto(*PILOTO_SECUNDARIO)

    yield db

    db.fechar()


@pytest.fixture
def validador(banco):
    """Módulo de validação apontando para o banco do teste."""
    return ValidationModule(banco)


@pytest.fixture
def servico(banco):
    """Serviço de agendamento integrado ao banco e ao validador do teste."""
    return BookingService(banco)


@pytest.fixture
def voo_base():
    """Dados de um voo válido, usados como ponto de partida nos cenários."""
    return {
        "prefixo": PREFIXO_LIBERADO,
        "piloto_id": 1,
        "origem": "Heliponto Jaraguá",
        "destino": "Plataforma P-52",
        "inicio": "2026-10-05T08:00",
        "fim": "2026-10-05T10:00",
        "peso_total_kg": 800,
    }

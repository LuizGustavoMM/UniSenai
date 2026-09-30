"""
Frota e constantes usadas pelas fixtures e pelos testes.

Fica em módulo separado para que tanto o conftest.py quanto o arquivo de
testes possam importar os mesmos valores sem importar o conftest duas vezes.
"""

PREFIXO_LIBERADO = "PT-HAA"
PREFIXO_EM_INSPECAO = "PT-HBB"
PREFIXO_POUCA_CAPACIDADE = "PT-HCC"

CAPACIDADE_LIBERADO = 1200
CAPACIDADE_EM_INSPECAO = 1000
CAPACIDADE_POUCA = 600

PILOTO_PRINCIPAL = ("Ana Ribeiro", "PLA-00123")
PILOTO_SECUNDARIO = ("Caio Ferraz", "PLA-00456")

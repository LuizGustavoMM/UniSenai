"""
frenagem_service.py

Modulo de Calculo de Desempenho de Frenagem de Pista do FMS (Flight Management
System). Calcula a distancia necessaria para a parada total da aeronave apos
o toque na pista, considerando a condicao da pista, o uso de reversores de
empuxo e a velocidade de toque (touchdown speed).

Regras de negocio (documentadas em detalhe no README.md):
  1. A distancia base e calculada a partir da fisica de frenagem simples:
         distancia_base = v^2 / (2 * DESACELERACAO_PADRAO_MS2)
     onde v e a velocidade de toque convertida de km/h para m/s.
  2. Sobre a distancia base sao aplicados dois fatores multiplicadores:
       - fator da condicao da pista (margem de seguranca regulatoria)
       - fator do uso de reversores de empuxo
  3. Se a velocidade de toque exceder o limite de seguranca (Vref + 20 km/h),
     o calculo padrao e recusado e o sistema emite um alerta de seguranca,
     exigindo autorizacao manual para arremetida (go-around).
  4. Entradas invalidas (velocidade negativa ou condicao de pista desconhecida)
     geram ValueError, nunca um resultado silencioso incorreto.
"""

from dataclasses import dataclass
from typing import Optional

# ---------------------------------------------------------------------------
# Constantes de regra de negocio
# ---------------------------------------------------------------------------

# Desaceleracao padrao de frenagem da aeronave (m/s^2), assumida para pista
# seca e sem uso de reversores. Usada como base do calculo fisico simplificado.
DESACELERACAO_PADRAO_MS2 = 2.5

# Margem de seguranca acrescida sobre a distancia base, conforme a condicao
# da pista (regra regulatoria simplificada para fins deste modulo).
FATOR_CONDICAO_PISTA = {
    "seca": 1.00,      # sem acrescimo
    "molhada": 1.15,   # +15%
    "gelo": 1.70,      # +70% (condicao extrema, conforme especificacao)
}

# Fator de reducao da distancia quando os reversores de empuxo estao ativos.
FATOR_REVERSORES = {
    True: 0.85,   # ativos: reduz 15% a distancia de frenagem
    False: 1.00,  # inativos: sem alteracao
}

# Margem de seguranca somada a Vref para definir a velocidade maxima de
# pouso permitida para o calculo padrao (Vref + 20 km/h).
MARGEM_VELOCIDADE_SEGURANCA_KMH = 20

CONDICOES_VALIDAS = tuple(FATOR_CONDICAO_PISTA.keys())


# ---------------------------------------------------------------------------
# Estrutura de retorno
# ---------------------------------------------------------------------------

@dataclass
class ResultadoFrenagem:
    """Representa o resultado do calculo de frenagem para um pouso."""

    status: str                      # "ok" ou "alerta_seguranca"
    distancia_m: Optional[float]      # None quando status == "alerta_seguranca"
    mensagem: str


# ---------------------------------------------------------------------------
# Funcao principal
# ---------------------------------------------------------------------------

def calcular_distancia_frenagem(
    condicao_pista: str,
    reversores_ativos: bool,
    velocidade_toque_kmh: float,
    vref_kmh: float,
) -> ResultadoFrenagem:
    """
    Calcula a distancia de frenagem necessaria para a parada total da
    aeronave apos o toque na pista.

    Args:
        condicao_pista: "seca", "molhada" ou "gelo".
        reversores_ativos: True se os reversores de empuxo estao ativados.
        velocidade_toque_kmh: velocidade de toque (touchdown speed) em km/h.
        vref_kmh: velocidade de referencia de pouso (Vref) em km/h.

    Returns:
        ResultadoFrenagem com status "ok" (e a distancia calculada) ou
        "alerta_seguranca" (quando a velocidade de toque excede Vref + 20).

    Raises:
        ValueError: se velocidade_toque_kmh ou vref_kmh forem negativas, ou
            se condicao_pista nao for uma das condicoes validas.
    """
    _validar_entradas(condicao_pista, velocidade_toque_kmh, vref_kmh)

    limite_seguranca_kmh = vref_kmh + MARGEM_VELOCIDADE_SEGURANCA_KMH

    if velocidade_toque_kmh > limite_seguranca_kmh:
        return ResultadoFrenagem(
            status="alerta_seguranca",
            distancia_m=None,
            mensagem=(
                f"ALERTA DE SEGURANCA: velocidade de toque "
                f"({velocidade_toque_kmh:.1f} km/h) excede o limite Vref + 20 "
                f"({limite_seguranca_kmh:.1f} km/h). Calculo padrao recusado. "
                f"Autorizacao manual para arremetida (go-around) requerida."
            ),
        )

    distancia_m = _calcular_distancia(
        condicao_pista, reversores_ativos, velocidade_toque_kmh
    )

    return ResultadoFrenagem(
        status="ok",
        distancia_m=distancia_m,
        mensagem=(
            f"Distancia de frenagem calculada: {distancia_m:.1f} m "
            f"(pista {condicao_pista}, reversores "
            f"{'ativos' if reversores_ativos else 'inativos'})."
        ),
    )


# ---------------------------------------------------------------------------
# Funcoes auxiliares
# ---------------------------------------------------------------------------

def _validar_entradas(
    condicao_pista: str, velocidade_toque_kmh: float, vref_kmh: float
) -> None:
    if velocidade_toque_kmh < 0 or vref_kmh < 0:
        raise ValueError(
            "Velocidade nao pode ser negativa "
            f"(velocidade_toque_kmh={velocidade_toque_kmh}, vref_kmh={vref_kmh})."
        )

    if condicao_pista not in FATOR_CONDICAO_PISTA:
        raise ValueError(
            f"Condicao de pista desconhecida: '{condicao_pista}'. "
            f"Valores validos: {CONDICOES_VALIDAS}."
        )


def _calcular_distancia(
    condicao_pista: str, reversores_ativos: bool, velocidade_toque_kmh: float
) -> float:
    velocidade_ms = velocidade_toque_kmh / 3.6
    distancia_base_m = (velocidade_ms ** 2) / (2 * DESACELERACAO_PADRAO_MS2)

    fator_pista = FATOR_CONDICAO_PISTA[condicao_pista]
    fator_reversor = FATOR_REVERSORES[reversores_ativos]

    return distancia_base_m * fator_pista * fator_reversor

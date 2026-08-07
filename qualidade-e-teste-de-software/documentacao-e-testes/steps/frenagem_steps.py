"""
steps/frenagem_steps.py

Step definitions (behave) que ligam os cenarios em Gherkin do arquivo
features/frenagem_pista.feature a implementacao real do modulo
frenagem_service.py.
"""

import math
from behave import given, when, then

from frenagem_service import calcular_distancia_frenagem


# ---------------------------------------------------------------------------
# Contexto (Dado)
# ---------------------------------------------------------------------------

@given('que a velocidade de referência (Vref) para este pouso é {vref:d} km/h')
def step_definir_vref(context, vref):
    context.vref_kmh = vref


@given('que a desaceleração padrão de frenagem da aeronave é {valor:g} m/s²')
def step_definir_desaceleracao(context, valor):
    # A desaceleracao padrao ja e uma constante interna de frenagem_service.py;
    # aqui apenas registramos o valor esperado para eventual conferencia.
    context.desaceleracao_esperada = valor


@given('que a condição da pista é "{condicao}"')
def step_definir_condicao_pista(context, condicao):
    context.condicao_pista = condicao


@given('que os reversores de empuxo estão "{estado}"')
def step_definir_reversores(context, estado):
    context.reversores_ativos = (estado == "ativos")


# ---------------------------------------------------------------------------
# Ação (Quando)
# ---------------------------------------------------------------------------

@when('a aeronave toca a pista a uma velocidade de {velocidade:g} km/h')
def step_calcular(context, velocidade):
    context.velocidade_toque_kmh = velocidade
    context.resultado = calcular_distancia_frenagem(
        condicao_pista=context.condicao_pista,
        reversores_ativos=context.reversores_ativos,
        velocidade_toque_kmh=velocidade,
        vref_kmh=context.vref_kmh,
    )


# ---------------------------------------------------------------------------
# Verificação (Então)
# ---------------------------------------------------------------------------

@then('o sistema deve calcular a distância de frenagem normalmente')
def step_verificar_status_ok(context):
    assert context.resultado.status == "ok", (
        f"Esperado status 'ok', obtido '{context.resultado.status}'"
    )


@then('a distância de frenagem calculada deve ser aproximadamente {esperado:g} metros')
def step_verificar_distancia(context, esperado):
    assert math.isclose(context.resultado.distancia_m, esperado, rel_tol=1e-3), (
        f"Esperado ~{esperado} m, obtido {context.resultado.distancia_m} m"
    )


@then('nenhum alerta de segurança deve ser emitido')
def step_verificar_sem_alerta(context):
    assert context.resultado.status != "alerta_seguranca"


@then('o sistema deve emitir um alerta de segurança no painel do piloto')
def step_verificar_alerta(context):
    assert context.resultado.status == "alerta_seguranca"


@then('o sistema deve recusar o cálculo padrão de distância de frenagem')
def step_verificar_recusa_calculo(context):
    assert context.resultado.distancia_m is None


@then('o sistema deve exigir autorização manual para arremetida "go-around"')
def step_verificar_exige_goaround(context):
    assert "go-around" in context.resultado.mensagem.lower() or \
        "arremetida" in context.resultado.mensagem.lower()


@then('essa distância deve ser {percentual:g}% maior do que a distância calculada nas mesmas condições em pista seca')
def step_verificar_incremento_percentual(context, percentual):
    resultado_gelo = context.resultado
    resultado_seca = calcular_distancia_frenagem(
        condicao_pista="seca",
        reversores_ativos=context.reversores_ativos,
        velocidade_toque_kmh=context.velocidade_toque_kmh,
        vref_kmh=context.vref_kmh,
    )
    fator_esperado = 1 + (percentual / 100)
    fator_obtido = resultado_gelo.distancia_m / resultado_seca.distancia_m
    assert math.isclose(fator_obtido, fator_esperado, rel_tol=1e-3), (
        f"Esperado incremento de {percentual}% (fator {fator_esperado}), "
        f"obtido fator {fator_obtido}"
    )
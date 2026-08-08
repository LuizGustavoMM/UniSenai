"""
tests/test_frenagem_service.py

Suite de testes unitarios (pytest) do modulo frenagem_service.py.
A justificativa de cada classe/caso de teste esta documentada na Matriz de
Testes, na secao correspondente do README.md.
"""

import pytest

from frenagem_service import calcular_distancia_frenagem, MARGEM_VELOCIDADE_SEGURANCA_KMH

VREF_PADRAO_KMH = 260


class TestCalculoDeDistancia:
    """Valida a formula de distancia de frenagem para cada condicao de pista."""

    def test_pista_seca_reversores_inativos(self):
        resultado = calcular_distancia_frenagem("seca", False, 240, VREF_PADRAO_KMH)
        assert resultado.status == "ok"
        assert resultado.distancia_m == pytest.approx(888.89, rel=1e-3)

    def test_pista_molhada_reversores_ativos(self):
        resultado = calcular_distancia_frenagem("molhada", True, 250, VREF_PADRAO_KMH)
        assert resultado.status == "ok"
        assert resultado.distancia_m == pytest.approx(942.8, rel=1e-3)

    def test_pista_gelo_incrementa_70_por_cento_em_relacao_a_pista_seca(self):
        resultado_gelo = calcular_distancia_frenagem("gelo", False, 240, VREF_PADRAO_KMH)
        resultado_seca = calcular_distancia_frenagem("seca", False, 240, VREF_PADRAO_KMH)
        assert resultado_gelo.distancia_m == pytest.approx(
            resultado_seca.distancia_m * 1.70, rel=1e-3
        )

    def test_reversores_ativos_reduzem_a_distancia_em_relacao_a_inativos(self):
        resultado_com_reversor = calcular_distancia_frenagem("seca", True, 240, VREF_PADRAO_KMH)
        resultado_sem_reversor = calcular_distancia_frenagem("seca", False, 240, VREF_PADRAO_KMH)
        assert resultado_com_reversor.distancia_m < resultado_sem_reversor.distancia_m


class TestValoresDeBorda:
    """
    Cobre o limite exato de velocidade permitida: Vref + 20 km/h.
    A regra de negocio e 'excede o limite', ou seja, o valor exatamente
    igual ao limite ainda deve ser aceito; apenas acima dele o sistema
    deve recusar o calculo e emitir o alerta de seguranca.
    """

    def test_velocidade_exatamente_no_limite_vref_mais_20_ainda_e_aceita(self):
        limite = VREF_PADRAO_KMH + MARGEM_VELOCIDADE_SEGURANCA_KMH
        resultado = calcular_distancia_frenagem("seca", False, limite, VREF_PADRAO_KMH)
        assert resultado.status == "ok"
        assert resultado.distancia_m is not None

    def test_velocidade_logo_acima_do_limite_dispara_alerta_de_seguranca(self):
        limite = VREF_PADRAO_KMH + MARGEM_VELOCIDADE_SEGURANCA_KMH
        resultado = calcular_distancia_frenagem("seca", False, limite + 0.1, VREF_PADRAO_KMH)
        assert resultado.status == "alerta_seguranca"
        assert resultado.distancia_m is None

    def test_velocidade_logo_abaixo_do_limite_nao_dispara_alerta(self):
        limite = VREF_PADRAO_KMH + MARGEM_VELOCIDADE_SEGURANCA_KMH
        resultado = calcular_distancia_frenagem("seca", False, limite - 0.1, VREF_PADRAO_KMH)
        assert resultado.status == "ok"


class TestTratamentoDeExcecoes:
    """Garante que entradas invalidas nunca produzem um resultado silencioso incorreto."""

    def test_velocidade_de_toque_negativa_gera_value_error(self):
        with pytest.raises(ValueError):
            calcular_distancia_frenagem("seca", False, -10, VREF_PADRAO_KMH)

    def test_vref_negativa_gera_value_error(self):
        with pytest.raises(ValueError):
            calcular_distancia_frenagem("seca", False, 200, -5)

    def test_condicao_de_pista_desconhecida_gera_value_error(self):
        with pytest.raises(ValueError):
            calcular_distancia_frenagem("gramado", False, 200, VREF_PADRAO_KMH)

"""
Suite de testes automatizados para o modulo estrategia_f1.

Organizacao:
    - Etapa 1 (Caixa Preta): casos derivados exclusivamente das regras de
      negocio descritas na especificacao, por particoes de equivalencia e
      analise de valor limite, sem olhar a implementacao.
    - Etapa 2 (Caixa Branca): casos adicionais derivados da estrutura do
      codigo, para fechar 100% de cobertura de instrucoes e de ramos.

O mapeamento CP## -> regra de negocio esta em MAPEAMENTO_TESTES.md.
"""

import pytest

from estrategia_f1 import (
    PNEU_HARD,
    PNEU_SOFT,
    PNEU_WET,
    calcular_estrategia_pit_stop,
)


# =========================================================================
# ETAPA 1. TESTES DE CAIXA PRETA (FUNCIONAIS)
# =========================================================================


class TestValidacaoDeEntradas:
    """Regra: voltas_totais em [30, 80] e temp_asfalto em [10, 60].

    Valores fora desses intervalos devem lancar ValueError.
    """

    @pytest.mark.parametrize("voltas", [30, 31, 55, 79, 80])
    def test_cp01_voltas_dentro_do_intervalo_sao_aceitas(self, voltas):
        """CP01 - Limites inclusivos: 30 e 80 sao valores VALIDOS."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=voltas,
            temp_asfalto=30,
            porcentagem_chuva=0,
            desgaste_pneu=10,
        )
        assert resultado["composto"] in (PNEU_WET, PNEU_SOFT, PNEU_HARD)

    @pytest.mark.parametrize("voltas", [29, 0, -1, 81, 100])
    def test_cp02_voltas_fora_do_intervalo_lancam_value_error(self, voltas):
        """CP02 - Valores abaixo de 30 ou acima de 80 sao invalidos."""
        with pytest.raises(ValueError):
            calcular_estrategia_pit_stop(
                voltas_totais=voltas,
                temp_asfalto=30,
                porcentagem_chuva=0,
                desgaste_pneu=10,
            )

    @pytest.mark.parametrize("temperatura", [10, 11, 35, 59, 60])
    def test_cp03_temperatura_dentro_do_intervalo_e_aceita(self, temperatura):
        """CP03 - Limites inclusivos: 10 C e 60 C sao valores VALIDOS."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=50,
            temp_asfalto=temperatura,
            porcentagem_chuva=0,
            desgaste_pneu=10,
        )
        assert resultado["composto"] in (PNEU_WET, PNEU_SOFT, PNEU_HARD)

    @pytest.mark.parametrize("temperatura", [9, 0, -5, 61, 120])
    def test_cp04_temperatura_fora_do_intervalo_lanca_value_error(
        self, temperatura
    ):
        """CP04 - Temperaturas abaixo de 10 C ou acima de 60 C sao invalidas."""
        with pytest.raises(ValueError):
            calcular_estrategia_pit_stop(
                voltas_totais=50,
                temp_asfalto=temperatura,
                porcentagem_chuva=0,
                desgaste_pneu=10,
            )

    def test_cp05_mensagem_de_erro_identifica_o_parametro_de_voltas(self):
        """CP05 - A excecao deve identificar qual parametro falhou."""
        with pytest.raises(ValueError, match="voltas_totais"):
            calcular_estrategia_pit_stop(20, 30, 0, 10)

    def test_cp06_mensagem_de_erro_identifica_o_parametro_de_temperatura(self):
        """CP06 - A excecao deve identificar qual parametro falhou."""
        with pytest.raises(ValueError, match="temp_asfalto"):
            calcular_estrategia_pit_stop(50, 5, 0, 10)

    def test_cp07_validacao_ocorre_antes_de_qualquer_calculo(self):
        """CP07 - Entrada invalida nao deve produzir estrategia parcial."""
        with pytest.raises(ValueError):
            calcular_estrategia_pit_stop(10, 5, 0, 10)


class TestEscolhaDePneuWet:
    """Regra: chuva >= 50% seleciona Wet, independente da temperatura."""

    @pytest.mark.parametrize("chuva", [50, 51, 75, 99, 100])
    def test_cp08_chuva_igual_ou_acima_de_50_seleciona_wet(self, chuva):
        """CP08 - Valor limite: exatamente 50% ja exige pneu Wet."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=60,
            temp_asfalto=30,
            porcentagem_chuva=chuva,
            desgaste_pneu=20,
        )
        assert resultado["composto"] == PNEU_WET

    @pytest.mark.parametrize("temperatura", [10, 24, 25, 40, 60])
    def test_cp09_wet_independe_da_temperatura(self, temperatura):
        """CP09 - Chuva alta prevalece sobre qualquer temperatura."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=60,
            temp_asfalto=temperatura,
            porcentagem_chuva=80,
            desgaste_pneu=20,
        )
        assert resultado["composto"] == PNEU_WET

    @pytest.mark.parametrize("desgaste", [0, 50, 70, 85, 100])
    def test_cp10_wet_independe_do_desgaste(self, desgaste):
        """CP10 - Chuva alta prevalece sobre qualquer desgaste."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=60,
            temp_asfalto=30,
            porcentagem_chuva=60,
            desgaste_pneu=desgaste,
        )
        assert resultado["composto"] == PNEU_WET

    def test_cp11_chuva_abaixo_de_50_nao_seleciona_wet(self):
        """CP11 - Valor limite: 49% ainda e pista seca."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=60,
            temp_asfalto=30,
            porcentagem_chuva=49,
            desgaste_pneu=20,
        )
        assert resultado["composto"] != PNEU_WET


class TestEscolhaDePneuSoft:
    """Regra: sem chuva (< 50%), temp < 25 C e desgaste >= 70% -> Soft."""

    @pytest.mark.parametrize("desgaste", [70, 71, 90, 100])
    def test_cp12_temperatura_baixa_com_desgaste_alto_seleciona_soft(
        self, desgaste
    ):
        """CP12 - Valor limite: desgaste de exatamente 70% ja exige Soft."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=40,
            temp_asfalto=20,
            porcentagem_chuva=10,
            desgaste_pneu=desgaste,
        )
        assert resultado["composto"] == PNEU_SOFT

    @pytest.mark.parametrize("temperatura", [10, 15, 24])
    def test_cp13_soft_exige_temperatura_abaixo_de_25(self, temperatura):
        """CP13 - Valor limite: 24 C ainda esta na faixa do Soft."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=40,
            temp_asfalto=temperatura,
            porcentagem_chuva=10,
            desgaste_pneu=75,
        )
        assert resultado["composto"] == PNEU_SOFT

    def test_cp14_temperatura_alta_com_desgaste_alto_nao_e_soft(self):
        """CP14 - As duas condicoes do Soft sao conjuntas (E logico).

        Com 30 C o pneu Soft nao deve ser escolhido, mesmo com desgaste alto.
        """
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=40,
            temp_asfalto=30,
            porcentagem_chuva=10,
            desgaste_pneu=75,
        )
        assert resultado["composto"] != PNEU_SOFT

    def test_cp15_temperatura_baixa_com_desgaste_baixo_e_stint_longo(self):
        """CP15 - As duas condicoes do Soft sao conjuntas (E logico).

        Temperatura baixa sozinha nao basta: com desgaste de 50% e mais de
        30 voltas restantes, a escolha correta e o pneu Hard.
        """
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=80,
            temp_asfalto=20,
            porcentagem_chuva=10,
            desgaste_pneu=50,
        )
        assert resultado["composto"] == PNEU_HARD


class TestEscolhaDePneuHard:
    """Regra: temp >= 25 C OU mais de 30 voltas restantes -> Hard."""

    @pytest.mark.parametrize("temperatura", [25, 26, 40, 60])
    def test_cp16_temperatura_igual_ou_acima_de_25_seleciona_hard(
        self, temperatura
    ):
        """CP16 - Valor limite: exatamente 25 C ja exige pneu Hard."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=40,
            temp_asfalto=temperatura,
            porcentagem_chuva=10,
            desgaste_pneu=60,
        )
        assert resultado["composto"] == PNEU_HARD

    def test_cp17_stint_longo_seleciona_hard_mesmo_com_pista_fria(self):
        """CP17 - Mais de 30 voltas ate o fim exigem composto duravel."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=80,
            temp_asfalto=15,
            porcentagem_chuva=0,
            desgaste_pneu=45,
        )
        assert resultado["composto"] == PNEU_HARD

    def test_cp18_corrida_curta_e_fria_sem_desgaste_nao_usa_hard(self):
        """CP18 - Nenhuma das condicoes do Hard satisfeita."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=40,
            temp_asfalto=20,
            porcentagem_chuva=10,
            desgaste_pneu=50,
        )
        assert resultado["composto"] == PNEU_SOFT


class TestAlertaDeParadaDeEmergencia:
    """Regra: desgaste >= 80% dispara alerta de parada obrigatoria."""

    @pytest.mark.parametrize("desgaste", [80, 81, 95, 100])
    def test_cp19_desgaste_critico_dispara_alerta(self, desgaste):
        """CP19 - Valor limite: exatamente 80% ja dispara o alerta."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=50,
            temp_asfalto=30,
            porcentagem_chuva=10,
            desgaste_pneu=desgaste,
        )
        assert resultado["alerta_emergencia"] is True
        assert "obrigatoria" in resultado["mensagem"].lower()

    @pytest.mark.parametrize("desgaste", [0, 40, 78, 79])
    def test_cp20_desgaste_abaixo_do_limite_nao_dispara_alerta(self, desgaste):
        """CP20 - Valor limite: 79% ainda nao dispara o alerta."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=50,
            temp_asfalto=30,
            porcentagem_chuva=10,
            desgaste_pneu=desgaste,
        )
        assert resultado["alerta_emergencia"] is False
        assert "obrigatoria" not in resultado["mensagem"].lower()

    def test_cp21_mensagem_normal_informa_a_volta_da_parada(self):
        """CP21 - Sem alerta, a mensagem traz a volta programada."""
        resultado = calcular_estrategia_pit_stop(
            voltas_totais=60,
            temp_asfalto=30,
            porcentagem_chuva=0,
            desgaste_pneu=40,
        )
        assert str(resultado["volta_pit_stop"]) in resultado["mensagem"]


class TestContratoDeRetorno:
    """Regra: a funcao retorna a volta de parada e o composto ideal."""

    def test_cp22_retorno_possui_as_chaves_esperadas(self):
        """CP22 - Estrutura do resultado."""
        resultado = calcular_estrategia_pit_stop(60, 30, 10, 40)
        assert set(resultado) == {
            "volta_pit_stop",
            "composto",
            "alerta_emergencia",
            "mensagem",
        }

    def test_cp23_volta_de_parada_esta_dentro_da_corrida(self):
        """CP23 - A volta sugerida e sempre uma volta valida da corrida."""
        resultado = calcular_estrategia_pit_stop(60, 30, 10, 40)
        assert 1 <= resultado["volta_pit_stop"] <= 60

    def test_cp24_desgaste_maior_antecipa_a_parada(self):
        """CP24 - Quanto maior o desgaste, mais cedo a parada."""
        cedo = calcular_estrategia_pit_stop(60, 30, 10, 70)
        tarde = calcular_estrategia_pit_stop(60, 30, 10, 20)
        assert cedo["volta_pit_stop"] < tarde["volta_pit_stop"]

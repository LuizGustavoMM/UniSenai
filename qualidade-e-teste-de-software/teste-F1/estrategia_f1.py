"""
Módulo de estratégia de corrida da telemetria de Fórmula 1.

Responsável por determinar a volta ideal de parada no pit stop e o composto
de pneu a ser utilizado, a partir dos dados de telemetria da corrida.

Regras de negócio implementadas (ver README.md):
    - Validação de entradas (voltas totais e temperatura do asfalto);
    - Escolha de pneus Wet / Soft / Hard;
    - Alerta de parada de emergência por desgaste crítico.
"""

# --- Limites de validação de entrada -------------------------------------
VOLTAS_MINIMAS = 30
VOLTAS_MAXIMAS = 80
TEMPERATURA_MINIMA = 10
TEMPERATURA_MAXIMA = 60

# --- Limiares das regras de negócio --------------------------------------
LIMIAR_CHUVA_WET = 50          # % de chuva a partir da qual usa-se pneu Wet
LIMIAR_TEMPERATURA_SOFT = 25   # °C abaixo do qual o pneu Soft é favorecido
LIMIAR_DESGASTE_SOFT = 70      # % de desgaste que favorece o pneu Soft
LIMIAR_DESGASTE_ALERTA = 80    # % de desgaste que dispara parada obrigatória
LIMIAR_VOLTAS_RESTANTES_HARD = 30  # voltas restantes que exigem pneu durável

# --- Compostos disponíveis ------------------------------------------------
PNEU_WET = "Wet"
PNEU_SOFT = "Soft"
PNEU_HARD = "Hard"


def validar_entradas(voltas_totais, temp_asfalto):
    """Valida os parâmetros de entrada da estratégia.

    Levanta ValueError quando o número total de voltas está fora do
    intervalo [30, 80] ou a temperatura do asfalto fora de [10, 60].
    """
    if voltas_totais < VOLTAS_MINIMAS or voltas_totais > VOLTAS_MAXIMAS:
        raise ValueError(
            "voltas_totais deve estar entre "
            f"{VOLTAS_MINIMAS} e {VOLTAS_MAXIMAS} (inclusive)."
        )

    if temp_asfalto < TEMPERATURA_MINIMA or temp_asfalto > TEMPERATURA_MAXIMA:
        raise ValueError(
            "temp_asfalto deve estar entre "
            f"{TEMPERATURA_MINIMA} e {TEMPERATURA_MAXIMA} (inclusive)."
        )


def calcular_volta_parada(voltas_totais, desgaste_pneu):
    """Calcula em qual volta a parada no pit stop deve acontecer.

    Quanto maior o desgaste atual do pneu, mais cedo a janela de parada se
    abre. O resultado é sempre uma volta válida dentro da corrida.
    """
    janela = voltas_totais * (100 - desgaste_pneu) / 100
    volta = int(round(janela))

    if volta < 1:
        volta = 1

    return volta


def escolher_composto(temp_asfalto, porcentagem_chuva, desgaste_pneu,
                      voltas_restantes):
    """Seleciona o composto de pneu ideal segundo as regras de negócio."""
    if porcentagem_chuva >= LIMIAR_CHUVA_WET:
        return PNEU_WET

    if (temp_asfalto < LIMIAR_TEMPERATURA_SOFT
            and desgaste_pneu >= LIMIAR_DESGASTE_SOFT):
        return PNEU_SOFT

    if (temp_asfalto >= LIMIAR_TEMPERATURA_SOFT
            or voltas_restantes > LIMIAR_VOLTAS_RESTANTES_HARD):
        return PNEU_HARD

    return PNEU_SOFT


def verificar_alerta_emergencia(desgaste_pneu):
    """Indica se o desgaste exige parada obrigatória na próxima volta."""
    return desgaste_pneu >= LIMIAR_DESGASTE_ALERTA


def calcular_estrategia_pit_stop(voltas_totais, temp_asfalto,
                                 porcentagem_chuva, desgaste_pneu):
    """Determina a estratégia de pit stop da equipe.

    Parâmetros
    ----------
    voltas_totais : int
        Número total de voltas da corrida. Deve estar entre 30 e 80.
    temp_asfalto : float
        Temperatura do asfalto em °C. Deve estar entre 10 e 60.
    porcentagem_chuva : float
        Probabilidade de chuva, de 0 a 100.
    desgaste_pneu : float
        Desgaste atual do pneu, de 0 a 100.

    Retorna
    -------
    dict
        {"volta_pit_stop": int, "composto": str,
         "alerta_emergencia": bool, "mensagem": str}

    Levanta
    -------
    ValueError
        Quando voltas_totais ou temp_asfalto estão fora dos limites.
    """
    validar_entradas(voltas_totais, temp_asfalto)

    volta_pit_stop = calcular_volta_parada(voltas_totais, desgaste_pneu)
    voltas_restantes = voltas_totais - volta_pit_stop

    composto = escolher_composto(
        temp_asfalto, porcentagem_chuva, desgaste_pneu, voltas_restantes
    )

    alerta_emergencia = verificar_alerta_emergencia(desgaste_pneu)

    if alerta_emergencia:
        mensagem = (
            "ALERTA: desgaste critico. Parada obrigatoria na proxima volta."
        )
    else:
        mensagem = f"Parada programada para a volta {volta_pit_stop}."

    return {
        "volta_pit_stop": volta_pit_stop,
        "composto": composto,
        "alerta_emergencia": alerta_emergencia,
        "mensagem": mensagem,
    }

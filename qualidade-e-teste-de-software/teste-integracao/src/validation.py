"""
Módulo de validação de elegibilidade de voo.

Aplica as regras de negócio da operação antes de qualquer gravação. Este
módulo consulta o banco através do Database, mas nunca escreve nele: a
decisão de gravar é do BookingService.

Regras aplicadas, nesta ordem:
    1. o período informado precisa ser coerente, com fim depois do início;
    2. a aeronave precisa existir no cadastro;
    3. a aeronave precisa estar com manutenção LIBERADO;
    4. o peso total não pode exceder a capacidade útil da aeronave;
    5. a aeronave não pode ter outro voo agendado no mesmo intervalo.
"""

from src.database import MANUTENCAO_LIBERADO


# ------------------------------------------------------------- exceções ---

class ErroDeValidacao(Exception):
    """Base de todas as reprovações de regra de negócio."""


class PeriodoInvalido(ErroDeValidacao):
    """O horário de fim não é posterior ao de início."""


class AeronaveNaoEncontrada(ErroDeValidacao):
    """O prefixo informado não existe no cadastro de aeronaves."""


class AeronaveEmManutencao(ErroDeValidacao):
    """A aeronave não está com a manutenção liberada."""


class CapacidadeExcedida(ErroDeValidacao):
    """O peso total de passageiros e carga excede a capacidade útil."""


class ConflitoDeHorario(ErroDeValidacao):
    """A aeronave já tem outro voo agendado que se sobrepõe ao período."""


# ------------------------------------------------------------- validador ---

class ValidationModule:
    """Aplica as regras de elegibilidade consultando o banco."""

    def __init__(self, database):
        self.database = database

    def validar_agendamento(self, prefixo, inicio, fim, peso_total_kg):
        """Valida a solicitação e devolve a aeronave aprovada.

        Levanta uma subclasse de ErroDeValidacao na primeira regra reprovada.
        """
        self._validar_periodo(inicio, fim)
        aeronave = self._validar_aeronave_existe(prefixo)
        self._validar_manutencao(aeronave)
        self._validar_capacidade(aeronave, peso_total_kg)
        self._validar_disponibilidade(aeronave, inicio, fim)
        return aeronave

    # ------------------------------------------------------------ regras ---

    def _validar_periodo(self, inicio, fim):
        if fim <= inicio:
            raise PeriodoInvalido(
                "O fim do voo (%s) deve ser posterior ao início (%s)." % (fim, inicio)
            )

    def _validar_aeronave_existe(self, prefixo):
        aeronave = self.database.buscar_aeronave_por_prefixo(prefixo)
        if aeronave is None:
            raise AeronaveNaoEncontrada(
                "Aeronave de prefixo %s não encontrada no cadastro." % prefixo
            )
        return aeronave

    def _validar_manutencao(self, aeronave):
        if aeronave["status_manutencao"] != MANUTENCAO_LIBERADO:
            raise AeronaveEmManutencao(
                "Aeronave %s está com status de manutenção %s e não pode voar."
                % (aeronave["prefixo"], aeronave["status_manutencao"])
            )

    def _validar_capacidade(self, aeronave, peso_total_kg):
        if peso_total_kg > aeronave["capacidade_kg"]:
            raise CapacidadeExcedida(
                "Peso total de %d kg excede a capacidade de %d kg da aeronave %s."
                % (peso_total_kg, aeronave["capacidade_kg"], aeronave["prefixo"])
            )

    def _validar_disponibilidade(self, aeronave, inicio, fim):
        conflitos = self.database.buscar_conflitos_de_horario(
            aeronave["id"], inicio, fim
        )
        if conflitos:
            existente = conflitos[0]
            raise ConflitoDeHorario(
                "Aeronave %s já possui o voo %d agendado de %s a %s."
                % (aeronave["prefixo"], existente["id"],
                   existente["inicio"], existente["fim"])
            )

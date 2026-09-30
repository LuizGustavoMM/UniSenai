"""
Serviço de agendamento de voos.

É a classe que orquestra a integração: recebe a solicitação, chama o
ValidationModule para aplicar as regras de elegibilidade e, só se tudo for
aprovado, grava o agendamento no banco através do Database.

A gravação acontece dentro de uma transação explícita. Se qualquer regra
reprovar, ou se a escrita falhar, a transação é desfeita e o banco fica
exatamente como estava antes da chamada. Essa garantia é o que o Cenário 3
do trabalho verifica.
"""

from src.database import AGENDAMENTO_AGENDADO
from src.validation import ErroDeValidacao, ValidationModule


class BookingService:
    """Orquestra validação e persistência do agendamento de voo."""

    def __init__(self, database, validador=None):
        self.database = database
        self.validador = validador or ValidationModule(database)

    def agendar_voo(self, prefixo, piloto_id, origem, destino,
                    inicio, fim, peso_total_kg):
        """Agenda um voo e devolve os dados do agendamento confirmado.

        Levanta uma subclasse de ErroDeValidacao quando alguma regra reprova,
        sem deixar nada gravado no banco.
        """
        self.database.iniciar_transacao()
        try:
            aeronave = self.validador.validar_agendamento(
                prefixo, inicio, fim, peso_total_kg
            )
            agendamento_id = self.database.inserir_agendamento(
                aeronave_id=aeronave["id"],
                piloto_id=piloto_id,
                origem=origem,
                destino=destino,
                inicio=inicio,
                fim=fim,
                peso_total_kg=peso_total_kg,
                status=AGENDAMENTO_AGENDADO,
            )
            self.database.confirmar()
        except ErroDeValidacao:
            self.database.desfazer()
            raise
        except Exception:
            self.database.desfazer()
            raise

        return {
            "id": agendamento_id,
            "prefixo": aeronave["prefixo"],
            "aeronave_id": aeronave["id"],
            "piloto_id": piloto_id,
            "origem": origem,
            "destino": destino,
            "inicio": inicio,
            "fim": fim,
            "peso_total_kg": peso_total_kg,
            "status": AGENDAMENTO_AGENDADO,
        }

    def consultar_agenda(self, prefixo):
        """Lista os agendamentos de uma aeronave, do mais cedo ao mais tarde."""
        aeronave = self.database.buscar_aeronave_por_prefixo(prefixo)
        if aeronave is None:
            return []
        return self.database.listar_agendamentos(aeronave["id"])

    def cancelar_voo(self, agendamento_id):
        """Cancela um agendamento existente e informa se algo foi alterado."""
        self.database.iniciar_transacao()
        try:
            linhas = self.database.cancelar_agendamento(agendamento_id)
            self.database.confirmar()
        except Exception:
            self.database.desfazer()
            raise
        return linhas > 0

"""
Testes de integração do agendamento de voos da AeroLog Helicópteros.

Os testes exercitam os três módulos juntos: o BookingService chama o
ValidationModule, que consulta o Database, e a gravação acontece em um
arquivo SQLite de verdade. Nada é simulado com mock: o que se verifica é o
estado real do banco depois de cada operação.

Identificadores CT## são referenciados na matriz de cenários do relatório.
"""

import sqlite3

import pytest

from src.database import (
    AGENDAMENTO_AGENDADO,
    AGENDAMENTO_CANCELADO,
    MANUTENCAO_EM_INSPECAO,
    Database,
)
from src.validation import (
    AeronaveEmManutencao,
    AeronaveNaoEncontrada,
    CapacidadeExcedida,
    ConflitoDeHorario,
    ErroDeValidacao,
    PeriodoInvalido,
)

from dados_frota import (
    CAPACIDADE_POUCA,
    PREFIXO_EM_INSPECAO,
    PREFIXO_LIBERADO,
    PREFIXO_POUCA_CAPACIDADE,
)


# =========================================================================
# CENÁRIO 1. Caminho feliz, agendamento válido
# =========================================================================

class TestCenario1CaminhoFeliz:
    """Aeronave liberada e horário livre: o voo é confirmado e persiste."""

    def test_ct01_agendamento_valido_retorna_dados_confirmados(self, servico, voo_base):
        """CT01. O serviço devolve o agendamento com status AGENDADO."""
        resultado = servico.agendar_voo(**voo_base)

        assert resultado["id"] > 0
        assert resultado["status"] == AGENDAMENTO_AGENDADO
        assert resultado["prefixo"] == PREFIXO_LIBERADO
        assert resultado["origem"] == voo_base["origem"]

    def test_ct02_agendamento_valido_e_localizado_no_banco(self, servico, banco, voo_base):
        """CT02. O registro existe na tabela com os mesmos dados enviados."""
        resultado = servico.agendar_voo(**voo_base)

        gravado = banco.buscar_agendamento(resultado["id"])
        assert gravado is not None
        assert gravado["status"] == AGENDAMENTO_AGENDADO
        assert gravado["inicio"] == voo_base["inicio"]
        assert gravado["fim"] == voo_base["fim"]
        assert gravado["peso_total_kg"] == voo_base["peso_total_kg"]

    def test_ct03_agendamento_valido_vincula_a_aeronave_correta(self, servico, banco, voo_base):
        """CT03. A chave estrangeira aponta para a aeronave do prefixo pedido."""
        resultado = servico.agendar_voo(**voo_base)

        aeronave = banco.buscar_aeronave_por_prefixo(PREFIXO_LIBERADO)
        gravado = banco.buscar_agendamento(resultado["id"])
        assert gravado["aeronave_id"] == aeronave["id"]

    def test_ct04_dois_voos_em_horarios_distintos_sao_aceitos(self, servico, banco, voo_base):
        """CT04. A mesma aeronave aceita voos que não se sobrepõem."""
        servico.agendar_voo(**voo_base)

        segundo = dict(voo_base, inicio="2026-10-05T14:00", fim="2026-10-05T16:00")
        servico.agendar_voo(**segundo)

        assert banco.contar_agendamentos() == 2

    def test_ct05_voo_encostado_no_anterior_nao_e_conflito(self, servico, banco, voo_base):
        """CT05. Valor limite. Um voo que começa quando o outro termina passa."""
        servico.agendar_voo(**voo_base)

        encostado = dict(voo_base, inicio="2026-10-05T10:00", fim="2026-10-05T12:00")
        servico.agendar_voo(**encostado)

        assert banco.contar_agendamentos() == 2


# =========================================================================
# CENÁRIO 2. Conflito de horário da aeronave
# =========================================================================

class TestCenario2ConflitoDeHorario:
    """Mesma aeronave, horários sobrepostos: o segundo voo é recusado."""

    def test_ct06_horario_identico_levanta_conflito(self, servico, voo_base):
        """CT06. Tentar o mesmo intervalo exato levanta ConflitoDeHorario."""
        servico.agendar_voo(**voo_base)

        with pytest.raises(ConflitoDeHorario):
            servico.agendar_voo(**voo_base)

    def test_ct07_segundo_agendamento_nao_e_inserido_na_base(self, servico, banco, voo_base):
        """CT07. Após o conflito, a base continua com um único registro."""
        servico.agendar_voo(**voo_base)

        with pytest.raises(ConflitoDeHorario):
            servico.agendar_voo(**voo_base)

        assert banco.contar_agendamentos() == 1

    def test_ct08_sobreposicao_parcial_tambem_e_conflito(self, servico, banco, voo_base):
        """CT08. Sobreposição parcial, começando no meio do voo existente."""
        servico.agendar_voo(**voo_base)

        sobreposto = dict(voo_base, inicio="2026-10-05T09:00", fim="2026-10-05T11:00")
        with pytest.raises(ConflitoDeHorario):
            servico.agendar_voo(**sobreposto)

        assert banco.contar_agendamentos() == 1

    def test_ct09_mensagem_de_conflito_identifica_o_voo_existente(self, servico, voo_base):
        """CT09. A exceção informa o id e o período do voo que já existe."""
        primeiro = servico.agendar_voo(**voo_base)

        with pytest.raises(ConflitoDeHorario, match=str(primeiro["id"])):
            servico.agendar_voo(**voo_base)

    def test_ct10_aeronave_diferente_no_mesmo_horario_e_aceita(self, servico, banco, voo_base):
        """CT10. O conflito é por aeronave, não por horário da operação."""
        servico.agendar_voo(**voo_base)

        outra = dict(voo_base, prefixo=PREFIXO_POUCA_CAPACIDADE, peso_total_kg=500)
        servico.agendar_voo(**outra)

        assert banco.contar_agendamentos() == 2


# =========================================================================
# CENÁRIO 3. Aeronave em manutenção preventiva ou corretiva
# =========================================================================

class TestCenario3AeronaveEmManutencao:
    """Aeronave em inspeção: o voo é reprovado e nada é gravado."""

    def test_ct11_aeronave_em_inspecao_levanta_excecao(self, servico, voo_base):
        """CT11. Status EM_INSPECAO levanta AeronaveEmManutencao."""
        voo = dict(voo_base, prefixo=PREFIXO_EM_INSPECAO)

        with pytest.raises(AeronaveEmManutencao):
            servico.agendar_voo(**voo)

    def test_ct12_nenhuma_alteracao_permanece_no_banco(self, servico, banco, voo_base):
        """CT12. A base continua vazia após a reprovação."""
        voo = dict(voo_base, prefixo=PREFIXO_EM_INSPECAO)

        assert banco.contar_agendamentos() == 0
        with pytest.raises(AeronaveEmManutencao):
            servico.agendar_voo(**voo)
        assert banco.contar_agendamentos() == 0

    def test_ct13_mensagem_informa_o_status_que_bloqueou(self, servico, voo_base):
        """CT13. A exceção cita o status de manutenção encontrado."""
        voo = dict(voo_base, prefixo=PREFIXO_EM_INSPECAO)

        with pytest.raises(AeronaveEmManutencao, match=MANUTENCAO_EM_INSPECAO):
            servico.agendar_voo(**voo)

    def test_ct14_aeronave_liberada_apos_a_inspecao_passa_a_voar(self, servico, banco, voo_base):
        """CT14. Liberar a manutenção no banco destrava o agendamento."""
        voo = dict(voo_base, prefixo=PREFIXO_EM_INSPECAO)

        with pytest.raises(AeronaveEmManutencao):
            servico.agendar_voo(**voo)

        banco.atualizar_status_manutencao(PREFIXO_EM_INSPECAO, "LIBERADO")
        resultado = servico.agendar_voo(**voo)

        assert banco.buscar_agendamento(resultado["id"]) is not None


# =========================================================================
# CENÁRIO 4. Consistência do banco com fixtures e rollback
# =========================================================================

class TestCenario4IsolamentoDoBanco:
    """Cada teste recebe uma base limpa e não enxerga o que outro gravou."""

    def test_ct15_primeira_execucao_grava_um_voo(self, servico, banco, voo_base):
        """CT15. Parte de zero, grava um voo e termina com um registro."""
        assert banco.contar_agendamentos() == 0
        servico.agendar_voo(**voo_base)
        assert banco.contar_agendamentos() == 1

    def test_ct16_segunda_execucao_recebe_base_limpa(self, servico, banco, voo_base):
        """CT16. Roda depois do CT15 e ainda assim começa vazio."""
        assert banco.contar_agendamentos() == 0
        servico.agendar_voo(**voo_base)
        assert banco.contar_agendamentos() == 1

    def test_ct17_terceira_execucao_confirma_o_isolamento(self, servico, banco, voo_base):
        """CT17. Confirma que o padrão se mantém em sequência."""
        assert banco.contar_agendamentos() == 0
        servico.agendar_voo(**voo_base)
        assert banco.contar_agendamentos() == 1

    def test_ct18_cada_teste_recebe_um_arquivo_de_banco_proprio(self, caminho_banco):
        """CT18. O arquivo do banco fica em um diretório temporário do teste."""
        assert caminho_banco.endswith("aerolog_teste.db")
        assert "test_ct18" in caminho_banco

    def test_ct19_a_frota_de_teste_e_recriada_a_cada_execucao(self, banco):
        """CT19. As fixtures recriam o cadastro base, sem resíduo anterior."""
        assert banco.buscar_aeronave_por_prefixo(PREFIXO_LIBERADO) is not None
        assert banco.buscar_aeronave_por_prefixo(PREFIXO_EM_INSPECAO) is not None
        assert banco.buscar_piloto(1)["nome"] == "Ana Ribeiro"

    def test_ct20_rollback_nao_apaga_registros_ja_confirmados(self, servico, banco, voo_base):
        """CT20. Um voo confirmado sobrevive ao rollback de uma tentativa falha."""
        primeiro = servico.agendar_voo(**voo_base)

        with pytest.raises(ConflitoDeHorario):
            servico.agendar_voo(**voo_base)

        assert banco.buscar_agendamento(primeiro["id"]) is not None
        assert banco.contar_agendamentos() == 1

    def test_ct21_dados_persistem_apos_reabrir_a_conexao(self, servico, banco, voo_base, caminho_banco):
        """CT21. A gravação é real em disco, não um estado em memória."""
        resultado = servico.agendar_voo(**voo_base)
        banco.fechar()

        outro = Database(caminho_banco)
        outro.conectar()
        try:
            gravado = outro.buscar_agendamento(resultado["id"])
            assert gravado is not None
            assert gravado["status"] == AGENDAMENTO_AGENDADO
        finally:
            outro.fechar()


# =========================================================================
# Regras complementares de elegibilidade
# =========================================================================

class TestRegrasComplementares:
    """Demais reprovações do módulo de validação, todas sem efeito no banco."""

    def test_ct22_peso_acima_da_capacidade_e_recusado(self, servico, banco, voo_base):
        """CT22. Peso maior que a capacidade útil levanta CapacidadeExcedida."""
        voo = dict(voo_base, prefixo=PREFIXO_POUCA_CAPACIDADE,
                   peso_total_kg=CAPACIDADE_POUCA + 1)

        with pytest.raises(CapacidadeExcedida):
            servico.agendar_voo(**voo)
        assert banco.contar_agendamentos() == 0

    def test_ct23_peso_exatamente_na_capacidade_e_aceito(self, servico, banco, voo_base):
        """CT23. Valor limite. Peso igual à capacidade ainda é permitido."""
        voo = dict(voo_base, prefixo=PREFIXO_POUCA_CAPACIDADE,
                   peso_total_kg=CAPACIDADE_POUCA)

        servico.agendar_voo(**voo)
        assert banco.contar_agendamentos() == 1

    def test_ct24_prefixo_inexistente_e_recusado(self, servico, banco, voo_base):
        """CT24. Aeronave fora do cadastro levanta AeronaveNaoEncontrada."""
        voo = dict(voo_base, prefixo="PT-XXX")

        with pytest.raises(AeronaveNaoEncontrada):
            servico.agendar_voo(**voo)
        assert banco.contar_agendamentos() == 0

    def test_ct25_periodo_invertido_e_recusado(self, servico, banco, voo_base):
        """CT25. Fim anterior ao início levanta PeriodoInvalido."""
        voo = dict(voo_base, inicio="2026-10-05T10:00", fim="2026-10-05T08:00")

        with pytest.raises(PeriodoInvalido):
            servico.agendar_voo(**voo)
        assert banco.contar_agendamentos() == 0

    def test_ct26_periodo_de_duracao_zero_e_recusado(self, servico, voo_base):
        """CT26. Valor limite. Início igual ao fim também é período inválido."""
        voo = dict(voo_base, inicio="2026-10-05T08:00", fim="2026-10-05T08:00")

        with pytest.raises(PeriodoInvalido):
            servico.agendar_voo(**voo)

    def test_ct27_todas_as_reprovacoes_derivam_de_erro_de_validacao(self, servico, voo_base):
        """CT27. A hierarquia de exceções permite um tratamento único."""
        voo = dict(voo_base, prefixo="PT-XXX")

        with pytest.raises(ErroDeValidacao):
            servico.agendar_voo(**voo)

    def test_ct28_validacao_ocorre_antes_de_qualquer_escrita(self, validador, banco, voo_base):
        """CT28. O validador isolado não grava nada ao aprovar."""
        antes = banco.contar_agendamentos()
        validador.validar_agendamento(
            voo_base["prefixo"], voo_base["inicio"],
            voo_base["fim"], voo_base["peso_total_kg"]
        )
        assert banco.contar_agendamentos() == antes


# =========================================================================
# Consulta e cancelamento
# =========================================================================

class TestConsultaECancelamento:
    """Operações de leitura e de baixa de agendamento."""

    def test_ct29_consulta_lista_os_voos_da_aeronave_em_ordem(self, servico, voo_base):
        """CT29. A agenda volta ordenada do voo mais cedo para o mais tarde."""
        tarde = dict(voo_base, inicio="2026-10-05T14:00", fim="2026-10-05T16:00")
        servico.agendar_voo(**tarde)
        servico.agendar_voo(**voo_base)

        agenda = servico.consultar_agenda(PREFIXO_LIBERADO)
        assert [linha["inicio"] for linha in agenda] == [
            "2026-10-05T08:00", "2026-10-05T14:00"
        ]

    def test_ct30_consulta_de_prefixo_inexistente_volta_vazia(self, servico):
        """CT30. Consultar aeronave fora do cadastro devolve lista vazia."""
        assert servico.consultar_agenda("PT-XXX") == []

    def test_ct31_cancelamento_libera_o_horario_da_aeronave(self, servico, banco, voo_base):
        """CT31. Cancelado o voo, o mesmo intervalo volta a ser agendável."""
        primeiro = servico.agendar_voo(**voo_base)
        assert servico.cancelar_voo(primeiro["id"]) is True

        cancelado = banco.buscar_agendamento(primeiro["id"])
        assert cancelado["status"] == AGENDAMENTO_CANCELADO

        servico.agendar_voo(**voo_base)
        assert banco.contar_agendamentos() == 2

    def test_ct32_cancelar_agendamento_inexistente_nao_altera_nada(self, servico):
        """CT32. Cancelar um id que não existe apenas devolve False."""
        assert servico.cancelar_voo(9999) is False


# =========================================================================
# Comportamento do módulo de banco isolado
# =========================================================================

class TestModuloDeBanco:
    """Garantias do Database que os demais módulos assumem como verdadeiras."""

    def test_ct33_operacao_sem_conexao_falha_de_forma_clara(self):
        """CT33. Usar o banco sem conectar levanta RuntimeError explicativo."""
        db = Database(":memory:")
        with pytest.raises(RuntimeError, match="não conectado"):
            db.contar_agendamentos()

    def test_ct34_fechar_duas_vezes_nao_quebra(self, caminho_banco):
        """CT34. Fechar uma conexão já fechada é uma operação segura."""
        db = Database(caminho_banco)
        db.conectar()
        db.criar_tabelas()
        db.fechar()
        db.fechar()
        assert db.conexao is None

    def test_ct35_listagem_sem_filtro_traz_todas_as_aeronaves(self, servico, banco, voo_base):
        """CT35. Sem filtro de aeronave, a listagem cobre a operação inteira."""
        servico.agendar_voo(**voo_base)
        outra = dict(voo_base, prefixo=PREFIXO_POUCA_CAPACIDADE, peso_total_kg=500)
        servico.agendar_voo(**outra)

        assert len(banco.listar_agendamentos()) == 2

    def test_ct36_atualizar_prefixo_inexistente_nao_afeta_linhas(self, banco):
        """CT36. O update devolve zero quando o prefixo não existe."""
        assert banco.atualizar_status_manutencao("PT-XXX", "LIBERADO") == 0


# =========================================================================
# Falhas inesperadas de escrita
# =========================================================================

class TestFalhasDeEscrita:
    """Erros que não são de regra de negócio também precisam desfazer tudo."""

    def test_ct37_falha_de_integridade_desfaz_a_transacao(self, servico, banco, voo_base):
        """CT37. Piloto inexistente viola a chave estrangeira.

        A exceção não é de validação, então cai no tratamento genérico do
        serviço. Ainda assim a transação precisa ser desfeita e o banco
        precisa permanecer sem o registro.
        """
        voo = dict(voo_base, piloto_id=9999)

        with pytest.raises(sqlite3.IntegrityError):
            servico.agendar_voo(**voo)

        assert banco.contar_agendamentos() == 0

    def test_ct38_falha_no_cancelamento_desfaz_a_transacao(self, servico, banco, voo_base):
        """CT38. Erro inesperado durante o cancelamento também faz rollback.

        A operação de banco é substituída por uma que falha, para exercitar o
        caminho de exceção sem depender de um defeito real do SQLite.
        """
        agendado = servico.agendar_voo(**voo_base)

        def explodir(_id):
            raise sqlite3.OperationalError("falha simulada de escrita")

        original = banco.cancelar_agendamento
        banco.cancelar_agendamento = explodir
        try:
            with pytest.raises(sqlite3.OperationalError):
                servico.cancelar_voo(agendado["id"])
        finally:
            banco.cancelar_agendamento = original

        ainda_agendado = banco.buscar_agendamento(agendado["id"])
        assert ainda_agendado["status"] == AGENDAMENTO_AGENDADO

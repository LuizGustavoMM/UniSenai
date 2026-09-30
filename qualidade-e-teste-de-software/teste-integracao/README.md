# Testes de Integração: Agendamento de Voos de Helicóptero

Trabalho prático de **Qualidade e Teste de Software**. Suíte de testes de
integração para o módulo de agendamento de voos da AeroLog Helicópteros,
exercitando os três componentes conectados diretamente em Python, com
persistência real em SQLite e sem nenhuma dependência de API HTTP.

| | |
|---|---|
| **Autor** | Luiz Gustavo Mella Massing |
| **Stack** | Python 3 · pytest · pytest-cov · SQLite |
| **Testes** | 38 casos de integração |
| **Cobertura** | 100% de instruções e 100% de ramos em `src/` |

---

## 1. Componentes integrados

| Módulo | Responsabilidade |
|---|---|
| `src/database.py` | Conexão e operações no SQLite: aeronaves, pilotos e agendamentos. Expõe o controle de transação. |
| `src/validation.py` | Regras de elegibilidade do voo. Consulta o banco, nunca escreve nele. |
| `src/booking_service.py` | Orquestra a validação e grava o agendamento dentro de uma transação. |

O fluxo de uma solicitação é sempre o mesmo:

```
BookingService.agendar_voo()
   -> abre transação no Database
   -> ValidationModule.validar_agendamento()
        -> consulta aeronaves e agendamentos no Database
   -> se aprovado: Database.inserir_agendamento(status=AGENDADO) e COMMIT
   -> se reprovado: ROLLBACK e a exceção sobe para quem chamou
```

## 2. Regras de negócio

As regras são avaliadas nesta ordem, e a primeira que reprova interrompe o
processo:

| Ordem | Regra | Exceção |
|---|---|---|
| 1 | O fim do voo deve ser posterior ao início | `PeriodoInvalido` |
| 2 | A aeronave deve existir no cadastro | `AeronaveNaoEncontrada` |
| 3 | A manutenção deve estar com status `LIBERADO` | `AeronaveEmManutencao` |
| 4 | O peso total não pode exceder a capacidade útil | `CapacidadeExcedida` |
| 5 | A aeronave não pode ter voo sobreposto no período | `ConflitoDeHorario` |

Todas herdam de `ErroDeValidacao`, o que permite tratar qualquer reprovação de
regra com um único `except`.

Dois voos são considerados sobrepostos quando um começa antes do outro terminar
e termina depois do outro começar. Voos encostados, em que um termina exatamente
no horário em que o outro começa, são permitidos.

## 3. Como executar

```bash
python -m venv .venv
source .venv/Scripts/activate      # Git Bash no Windows
# source .venv/bin/activate        # Linux e macOS

pip install -r requirements.txt

pytest -v
pytest --cov=src --cov-report=term-missing
pytest --cov=src --cov-report=html     # abre em htmlcov/index.html
```

A cobertura de ramos está habilitada no `.coveragerc`, portanto os comandos
acima já reportam instruções e ramos juntos.

## 4. Estrutura

```
.
├── src/
│   ├── database.py              # Módulo de banco de dados (SQLite)
│   ├── validation.py            # Regras de elegibilidade da aeronave
│   └── booking_service.py       # Serviço de agendamento de voos
├── tests/
│   ├── conftest.py              # Fixtures do pytest, banco temporário por teste
│   ├── dados_frota.py           # Frota e constantes usadas nos testes
│   └── test_flight_integration.py
├── .coveragerc
├── .gitignore
├── pytest.ini
├── README.md
└── requirements.txt
```

## 5. Isolamento entre testes

Cada teste recebe um arquivo SQLite próprio, criado pela fixture `banco` dentro
do `tmp_path` daquele teste. O pytest destrói o diretório ao final, então nenhum
teste enxerga o que outro gravou.

A escolha foi por arquivo em disco, e não por banco em memória, justamente para
exercitar o caminho real de persistência: abertura de conexão, `BEGIN`, `COMMIT`
e `ROLLBACK` sobre um arquivo de verdade. O caso CT21 fecha a conexão, reabre o
mesmo arquivo com outra instância do `Database` e confirma que o registro
continua lá.

# Módulo de Cálculo de Desempenho de Frenagem de Pista

Documentação técnica do módulo de **Cálculo de Desempenho de Frenagem de Pista**
do Sistema de Gestão de Voo (FMS), responsável por calcular a distância
necessária para a parada total da aeronave durante a fase de aproximação e
pouso, antes de o código seguir para homologação junto à autoridade de
aviação civil (ANAC / FAA).

## Sumário

- [Contexto do Módulo](#contexto-do-módulo)
- [Estrutura do Repositório](#estrutura-do-repositório)
- [Regra de Negócio](#regra-de-negócio)
- [Matriz de Casos de Teste (TDD)](#matriz-de-casos-de-teste-tdd)
- [Guia de Homologação e Execução](#guia-de-homologação-e-execução)

## Contexto do Módulo

Durante o pouso, o módulo recebe três informações de entrada:

1. **Condição da pista**: seca, molhada ou com gelo.
2. **Uso de reversores de empuxo**: ativados ou inativos.
3. **Velocidade de toque (touchdown speed)**: comparada contra a velocidade de
   referência de pouso (Vref) somada a uma margem de segurança de 20 km/h.

Caso a velocidade de toque exceda `Vref + 20 km/h`, o módulo **recusa o
cálculo padrão** e emite um **alerta de segurança** no painel do piloto,
exigindo autorização manual para arremetida (*go-around*). Uma falha de
documentação ou de cobertura de testes nesse módulo pode levar a um
*runway excursion* (quando a aeronave ultrapassa o fim da pista).

## Estrutura do Repositório

```
.
├── frenagem_service.py          # Lógica de negócio do módulo
├── requirements.txt              # Dependências (behave e pytest)
├── pytest.ini                    # Configuração do pytest (ver nota abaixo)
├── conftest.py                   # Marcador de raiz do projeto para o pytest
├── features/
│   └── frenagem_pista.feature    # Especificação de comportamento (BDD / Gherkin)
├── steps/
│   └── frenagem_steps.py         # Step definitions do behave
└── tests/
    └── test_frenagem_service.py  # Testes unitários (pytest / TDD)
```

> **Nota sobre `pytest.ini` e `conftest.py`:** por padrão, o `pytest` só
> adiciona automaticamente ao `sys.path` a pasta onde está o arquivo de
> teste (`tests/`), e não a raiz do projeto, já que `tests/` não é um
> pacote Python (não tem `__init__.py`). Sem essa configuração, o import
> `from frenagem_service import ...` falharia com
> `ModuleNotFoundError: No module named 'frenagem_service'` ao rodar
> `pytest` a partir da raiz do repositório. O `pytest.ini` (com
> `pythonpath = .`) resolve isso adicionando explicitamente a raiz do
> projeto ao caminho de importação.

## Regra de Negócio

A distância de frenagem é calculada em duas etapas:

1. **Distância base** (física simplificada de frenagem):

   ```
   distancia_base = velocidade_toque_ms² / (2 × 2.5)
   ```

   onde `2.5 m/s²` é a desaceleração padrão de frenagem da aeronave, e a
   velocidade de toque é convertida de km/h para m/s.

2. **Fatores multiplicadores** aplicados sobre a distância base:

   | Condição da pista | Fator (margem de segurança) |
   |---|---|
   | Seca | 1,00 (sem acréscimo) |
   | Molhada | 1,15 (+15%) |
   | Com gelo | 1,70 (+70%) |

   | Reversores de empuxo | Fator |
   |---|---|
   | Ativos | 0,85 (reduz 15% a distância) |
   | Inativos | 1,00 |

   ```
   distancia_final = distancia_base × fator_condicao_pista × fator_reversores
   ```

3. **Verificação de segurança**: antes de calcular a distância, o módulo
   verifica se `velocidade_toque_kmh > vref_kmh + 20`. Se verdadeiro, o
   cálculo padrão é recusado e um alerta de segurança é retornado no lugar
   da distância.

4. **Validação de entradas**: velocidades negativas ou condição de pista fora
   de `{seca, molhada, gelo}` geram `ValueError` explícito, nunca um cálculo
   silenciosamente incorreto.

## Matriz de Casos de Teste (TDD)

Os testes unitários (`pytest`) validam diretamente as funções de
`frenagem_service.py`, com foco especial nos dois pontos mais sensíveis à
segurança do módulo: **valores de borda** na velocidade limite e
**tratamento de exceções** para entradas inválidas.

| # | Caso de Teste (`tests/test_frenagem_service.py`) | Função Testada | Categoria | Entrada | Resultado Esperado | Justificativa Técnica |
|---|---|---|---|---|---|---|
| 1 | `test_pista_seca_reversores_inativos` | `calcular_distancia_frenagem` | Regra de negócio | Pista seca, reversores inativos, 240 km/h | Distância ≈ 888,9 m | Confere o cálculo físico base, sem nenhum fator multiplicador aplicado (caso de referência). |
| 2 | `test_pista_molhada_reversores_ativos` | `calcular_distancia_frenagem` | Regra de negócio | Pista molhada, reversores ativos, 250 km/h | Distância ≈ 942,8 m | Confere a combinação dos dois fatores (+15% da pista, -15% dos reversores) na mesma chamada. |
| 3 | `test_pista_gelo_incrementa_70_por_cento_em_relacao_a_pista_seca` | `calcular_distancia_frenagem` | Regra de negócio | Pista com gelo vs. pista seca, mesma velocidade | Distância do gelo = 1,70 × distância da pista seca | Garante especificamente o requisito crítico do enunciado: o incremento de 70% na condição extrema de pista com gelo. |
| 4 | `test_reversores_ativos_reduzem_a_distancia_em_relacao_a_inativos` | `calcular_distancia_frenagem` | Regra de negócio | Mesma pista e velocidade, variando só os reversores | Distância com reversor < distância sem reversor | Isola o efeito dos reversores, garantindo que o fator realmente reduz a distância. |
| 5 | `test_velocidade_exatamente_no_limite_vref_mais_20_ainda_e_aceita` | `calcular_distancia_frenagem` | **Valor de borda** | `velocidade_toque = Vref + 20` (exatamente) | `status == "ok"`, calcula distância normalmente | A regra de negócio é "**excede** o limite"; testar exatamente o limite garante que o sistema não seja mais restritivo do que o especificado (falso alerta em condição ainda segura). |
| 6 | `test_velocidade_logo_acima_do_limite_dispara_alerta_de_seguranca` | `calcular_distancia_frenagem` | **Valor de borda** | `velocidade_toque = Vref + 20,1` | `status == "alerta_seguranca"`, `distancia_m is None` | Garante que o sistema reage corretamente assim que o limite é ultrapassado, sem margem de tolerância indevida — o ponto mais crítico de segurança do módulo. |
| 7 | `test_velocidade_logo_abaixo_do_limite_nao_dispara_alerta` | `calcular_distancia_frenagem` | **Valor de borda** | `velocidade_toque = Vref + 19,9` | `status == "ok"` | Fecha a análise de borda pelos dois lados do limite (imediatamente abaixo e imediatamente acima), evitando erros de "off-by-one" na comparação. |
| 8 | `test_velocidade_de_toque_negativa_gera_value_error` | `calcular_distancia_frenagem` | **Tratamento de exceção** | `velocidade_toque_kmh = -10` | Levanta `ValueError` | Velocidade negativa é fisicamente impossível; o módulo não pode aceitar silenciosamente um valor de sensor corrompido ou uma falha de integração. |
| 9 | `test_vref_negativa_gera_value_error` | `calcular_distancia_frenagem` | **Tratamento de exceção** | `vref_kmh = -5` | Levanta `ValueError` | Mesma justificativa do caso anterior, aplicada ao parâmetro de referência recebido de outro subsistema da aeronave. |
| 10 | `test_condicao_de_pista_desconhecida_gera_value_error` | `calcular_distancia_frenagem` | **Tratamento de exceção** | `condicao_pista = "gramado"` | Levanta `ValueError` | Impede que uma condição de pista não prevista (erro de integração ou nova condição ainda não homologada) resulte em um cálculo de distância usando um fator inválido ou ausente. |

## Guia de Homologação e Execução

Passo a passo para a equipe de auditoria clonar o repositório, instalar o
ambiente e executar tanto os testes de validação de regras de aviação
(`behave`) quanto os testes unitários da arquitetura (`pytest`).

### 1. Pré-requisitos

- Python 3.10 ou superior instalado.
- `git` instalado.

### 2. Clonar o repositório

```bash
git clone <URL-DO-REPOSITORIO>
cd <NOME-DO-REPOSITORIO>
```

### 3. Criar e ativar um ambiente virtual

```bash
python3 -m venv venv

# Linux / macOS
source venv/bin/activate

# Windows (PowerShell)
venv\Scripts\Activate.ps1
```

### 4. Instalar as dependências

```bash
pip install -r requirements.txt
```

### 5. Executar os testes de validação de regras de aviação (BDD / behave)

Valida os cenários de negócio descritos em `features/frenagem_pista.feature`
(pouso em pista molhada, alerta de segurança e condição extrema de gelo):

```bash
behave
```

Saída esperada: os 3 cenários listados como `3 scenarios passed, 0 failed`.

### 6. Executar os testes unitários da arquitetura (TDD / pytest)

Valida diretamente as funções internas de `frenagem_service.py`, incluindo
valores de borda e tratamento de exceções:

```bash
pytest -v
```

Saída esperada: os 10 casos da Matriz de Testes acima como `10 passed`.

### 7. Critério de homologação

O módulo só deve seguir para homologação junto à ANAC/FAA se **ambas** as
execuções (`behave` e `pytest`) terminarem sem nenhuma falha. Qualquer teste
vermelho, especialmente nos casos de borda ou de tratamento de exceção,
deve bloquear o avanço do módulo, dado o risco de um *runway excursion* em
caso de falha não detectada.

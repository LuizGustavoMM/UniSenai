# Relatório dos Bugs Identificados no Código Base

Etapa 3 do trabalho prático. A suíte de testes desenvolvida na Etapa 1 foi
executada contra o **código base original** e reprovou **8 execuções de
teste**, que se agrupam em **3 defeitos distintos** na lógica da aplicação.

Resultado da execução contra o código original:

```
81 execuções de teste: 73 passaram, 8 falharam
```

| # | Defeito | Função afetada | Tipo | Testes que o revelaram |
|---|---|---|---|---|
| 1 | Limites de voltas tratados como exclusivos | `validar_entradas` | erro de valor limite (*off-by-one*) | CP01 (30 e 80) |
| 2 | Limiar de chuva com comparação estrita | `escolher_composto` | operador relacional incorreto | CP08 (50), CP33 |
| 3 | Condição do pneu Soft com `OU` em vez de `E` | `escolher_composto` | operador lógico incorreto | CP14, CP15, CP17, CP36 |

---

## Bug #1. Limites de `voltas_totais` tratados como exclusivos

**Localização:** `estrategia_f1.validar_entradas`

**Código com defeito**

```python
if voltas_totais <= VOLTAS_MINIMAS or voltas_totais >= VOLTAS_MAXIMAS:
    raise ValueError(...)
```

**Descrição**

A especificação define que o número total de voltas deve estar entre 30 e 80
**inclusive**. O código usava `<=` e `>=`, o que rejeita exatamente os dois
valores de fronteira válidos. Uma corrida de 30 voltas ou de 80 voltas,
ambas legítimas, era recusada com `ValueError`.

**Como foi detectado**

O caso CP01 (`test_cp01_voltas_dentro_do_intervalo_sao_aceitas`) aplica
análise de valor limite e verifica que 30 e 80 são aceitos. Duas das cinco
execuções parametrizadas falharam:

```
CP01[30] - ValueError: voltas_totais deve estar entre 30 e 80 (inclusive).
CP01[80] - ValueError: voltas_totais deve estar entre 30 e 80 (inclusive).
```

**Impacto**

Bloqueio total do cálculo de estratégia em corridas nos limites do
regulamento, com falso positivo de entrada inválida.

**Correção aplicada**

```python
if voltas_totais < VOLTAS_MINIMAS or voltas_totais > VOLTAS_MAXIMAS:
    raise ValueError(...)
```

---

## Bug #2. Limiar de chuva com comparação estrita

**Localização:** `estrategia_f1.escolher_composto`

**Código com defeito**

```python
if porcentagem_chuva > LIMIAR_CHUVA_WET:
    return PNEU_WET
```

**Descrição**

A regra diz: *"se a probabilidade de chuva for **≥ 50%**, a função deve
selecionar o pneu Wet"*. Com `>`, uma probabilidade de exatamente 50% caía
nas regras de pista seca e retornava `Soft` ou `Hard`.

**Como foi detectado**

- CP08 (`test_cp08_chuva_igual_ou_acima_de_50_seleciona_wet`) com
  `chuva = 50`, falhou (`'Hard' != 'Wet'`).
- CP33 (`test_cp33_caminho_1_retorna_wet`), teste de caixa branca que
  exercita o primeiro caminho de saída da função, falhou.

**Impacto**

Defeito crítico de segurança operacional: em pista molhada no limiar da
regra, o carro seria enviado à pista com composto de piso seco.

**Correção aplicada**

```python
if porcentagem_chuva >= LIMIAR_CHUVA_WET:
    return PNEU_WET
```

---

## Bug #3. Condição do pneu Soft usando `OU` em vez de `E`

**Localização:** `estrategia_f1.escolher_composto`

**Código com defeito**

```python
if (temp_asfalto < LIMIAR_TEMPERATURA_SOFT
        or desgaste_pneu >= LIMIAR_DESGASTE_SOFT):
    return PNEU_SOFT
```

**Descrição**

A regra exige as **duas** condições simultaneamente: *"seleciona-se o pneu
Soft quando a temperatura do asfalto for inferior a 25 °C **e** o desgaste do
pneu for ≥ 70%"*. Com o operador `or`, qualquer uma das condições isolada já
bastava, e o pneu Soft passava a ser escolhido em dois cenários errados:

- pista quente (≥ 25 °C) com pneu gasto, quando deveria ser `Hard`;
- pista fria com pneu praticamente novo e stint longo, quando deveria ser
  `Hard`.

Como esse `if` vem antes da regra do `Hard`, o retorno prematuro mascarava
completamente a regra 3 nesses casos.

**Como foi detectado**

| Teste | Cenário | Esperado | Obtido |
|---|---|---|---|
| CP14 | `temp = 30`, `desgaste = 75` | ≠ `Soft` | `Soft` |
| CP15 | 80 voltas, `temp = 20`, `desgaste = 50` | `Hard` | `Soft` |
| CP17 | 80 voltas, `temp = 15`, `desgaste = 45` | `Hard` | `Soft` |
| CP36 | `voltas_restantes = 31` | `Hard` | `Soft` |

**Impacto**

Escolha de composto macio em condições de alta degradação térmica ou em
stints longos, com desgaste acelerado e parada adicional não planejada.

**Correção aplicada**

```python
if (temp_asfalto < LIMIAR_TEMPERATURA_SOFT
        and desgaste_pneu >= LIMIAR_DESGASTE_SOFT):
    return PNEU_SOFT
```

---

## Situação após as correções

```
81 execuções de teste: 81 passaram, 0 falharam
Cobertura de instruções: 100%
Cobertura de ramos:      100%
```

O histórico de commits do repositório registra cada correção em um commit
separado (`fix: ...`), permitindo rastrear defeito, teste e correção.

# Mapeamento dos Casos de Teste

Rastreabilidade entre as regras de negócio da especificação e os casos de
teste implementados em `test_estrategia.py`.

A suíte tem **39 casos de teste**, que se expandem em **81 execuções** por
conta dos `@pytest.mark.parametrize`.

---

## Etapa 1. Testes de Caixa Preta (funcionais)

Casos derivados **exclusivamente da especificação**, por *particionamento de
equivalência* (PE) e *análise de valor limite* (AVL).

### R1. Validação de entradas

| CP | Técnica | Entrada | Resultado esperado |
|---|---|---|---|
| CP01 | AVL | `voltas_totais` ∈ {30, 31, 55, 79, 80} | aceita (limites são **inclusivos**) |
| CP02 | AVL/PE | `voltas_totais` ∈ {29, 0, -1, 81, 100} | `ValueError` |
| CP03 | AVL | `temp_asfalto` ∈ {10, 11, 35, 59, 60} | aceita (limites são **inclusivos**) |
| CP04 | AVL/PE | `temp_asfalto` ∈ {9, 0, -5, 61, 120} | `ValueError` |
| CP05 | robustez | `voltas_totais = 20` | mensagem cita `voltas_totais` |
| CP06 | robustez | `temp_asfalto = 5` | mensagem cita `temp_asfalto` |
| CP07 | robustez | dois parâmetros inválidos | `ValueError` (sem resultado parcial) |

### R2. Escolha de pneu de chuva (Wet)

| CP | Técnica | Entrada | Resultado esperado |
|---|---|---|---|
| CP08 | AVL | `chuva` ∈ {50, 51, 75, 99, 100} | `Wet` (50% **já** é Wet) |
| CP09 | PE | `chuva = 80`, `temp` ∈ {10, 24, 25, 40, 60} | `Wet` em todas |
| CP10 | PE | `chuva = 60`, `desgaste` ∈ {0, 50, 70, 85, 100} | `Wet` em todas |
| CP11 | AVL | `chuva = 49` | **não** é `Wet` |

### R3. Escolha de pneu macio (Soft)

| CP | Técnica | Entrada | Resultado esperado |
|---|---|---|---|
| CP12 | AVL | `temp = 20`, `desgaste` ∈ {70, 71, 90, 100} | `Soft` (70% **já** é Soft) |
| CP13 | AVL | `desgaste = 75`, `temp` ∈ {10, 15, 24} | `Soft` (24 °C ainda é Soft) |
| CP14 | PE | `temp = 30`, `desgaste = 75` | **não** é `Soft` (condição é um **E**) |
| CP15 | PE | `temp = 20`, `desgaste = 50`, 80 voltas | `Hard` (condição é um **E**) |

### R4. Escolha de pneu duro (Hard)

| CP | Técnica | Entrada | Resultado esperado |
|---|---|---|---|
| CP16 | AVL | `temp` ∈ {25, 26, 40, 60} | `Hard` (25 °C **já** é Hard) |
| CP17 | PE | 80 voltas, `temp = 15`, `desgaste = 45` | `Hard` (mais de 30 voltas restantes) |
| CP18 | PE | 40 voltas, `temp = 20`, `desgaste = 50` | `Soft` (nenhuma condição do Hard) |

### R5. Alerta de parada de emergência

| CP | Técnica | Entrada | Resultado esperado |
|---|---|---|---|
| CP19 | AVL | `desgaste` ∈ {80, 81, 95, 100} | alerta `True` + mensagem obrigatória |
| CP20 | AVL | `desgaste` ∈ {0, 40, 78, 79} | alerta `False` |
| CP21 | funcional | `desgaste = 40` | mensagem informa a volta programada |

### R6. Contrato de retorno

| CP | Técnica | Entrada | Resultado esperado |
|---|---|---|---|
| CP22 | contrato | entrada válida | chaves `volta_pit_stop`, `composto`, `alerta_emergencia`, `mensagem` |
| CP23 | contrato | 60 voltas | `1 <= volta_pit_stop <= 60` |
| CP24 | funcional | `desgaste` 70 vs 20 | desgaste maior antecipa a parada |

---

## Etapa 2. Testes de Caixa Branca (estruturais)

Casos derivados da **estrutura interna** do código. Enquanto os testes de
caixa preta exercitam a função pública de ponta a ponta, estes atacam cada
função auxiliar isoladamente, garantindo que **todo ramo tenha sido tomado
nos dois sentidos** e que cada sub-condição dos operadores lógicos seja
responsável, sozinha, por um resultado observado (cobertura de condição).

Sem esta etapa a suíte ainda alcançaria cobertura de instruções e de ramos,
mas os defeitos ficariam mascarados por caminhos coincidentes, que foi
justamente o que aconteceu com o Bug #3, em que o retorno prematuro do
`Soft` escondia inteiramente a regra do `Hard`.

| CP | Alvo | Ramo / caminho exercitado |
|---|---|---|
| CP25 | `validar_entradas` | ambos os `if` falsos (caminho feliz) |
| CP26 | `validar_entradas` | 1ª sub-condição do 1º `if` (`voltas < 30`) |
| CP27 | `validar_entradas` | 2ª sub-condição do 1º `if` (`voltas > 80`) |
| CP28 | `validar_entradas` | 1ª sub-condição do 2º `if` (`temp < 10`) |
| CP29 | `validar_entradas` | 2ª sub-condição do 2º `if` (`temp > 60`) |
| CP30 | `calcular_volta_parada` | `if volta < 1` falso |
| CP31 | `calcular_volta_parada` | `if volta < 1` verdadeiro (saturação) |
| CP32 | `calcular_volta_parada` | arredondamento da janela fracionária |
| CP33 | `escolher_composto` | caminho 1, retorno `Wet` |
| CP34 | `escolher_composto` | caminho 2, retorno `Soft` |
| CP35 | `escolher_composto` | caminho 3 pela 1ª sub-condição (temperatura) |
| CP36 | `escolher_composto` | caminho 3 pela 2ª sub-condição (voltas restantes) |
| CP37 | `escolher_composto` | caminho 4, retorno final por padrão |
| CP38 | `verificar_alerta_emergencia` | resultado `True` no limite (80) |
| CP39 | `verificar_alerta_emergencia` | resultado `False` abaixo do limite (79) |

---

## Resultado da cobertura

| Métrica | Resultado |
|---|---|
| Cobertura de instruções (*statement*) | **100%** |
| Cobertura de ramos (*branch*) | **100%** |

Comando utilizado:

```bash
pytest --cov=estrategia_f1 --cov-report=term-missing
```

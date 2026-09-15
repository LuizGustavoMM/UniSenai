# Testes em Telemetria de F1 com PyTest & Coverage

Trabalho prático de **Verificação e Validação de Software**. Suíte de testes
automatizados para o módulo de estratégia de corrida de uma equipe de
Fórmula 1.

| | |
|---|---|
| **Módulo sob teste** | `estrategia_f1.py` |
| **Suíte de testes** | `test_estrategia.py` |
| **Stack** | Python 3 · pytest · pytest-cov |
| **Cobertura** | 100% de instruções e 100% de ramos |

## Autor

Luiz Gustavo Mella Massing

---

## 1. Regras de negócio

A função `calcular_estrategia_pit_stop(voltas_totais, temp_asfalto,
porcentagem_chuva, desgaste_pneu)` determina a volta de parada e o composto
de pneu ideal.

### Validação de entradas

- `voltas_totais` deve estar entre **30 e 80** (inclusive).
- `temp_asfalto` deve estar entre **10 °C e 60 °C** (inclusive).
- Fora desses limites a função lança `ValueError`.

### Escolha do composto

As regras são avaliadas **nesta ordem de precedência**:

| Ordem | Condição | Composto |
|---|---|---|
| 1 | `porcentagem_chuva >= 50`, independe da temperatura | `Wet` |
| 2 | `temp_asfalto < 25` **E** `desgaste_pneu >= 70` | `Soft` |
| 3 | `temp_asfalto >= 25` **OU** mais de 30 voltas restantes | `Hard` |
| 4 | nenhuma das anteriores (pista fria, stint curto, pneu inteiro) | `Soft` |

### Alerta de parada de emergência

Se `desgaste_pneu >= 80`, o resultado traz `alerta_emergencia = True` e a
mensagem de **parada obrigatória na próxima volta**.

### Volta de parada

A janela de parada é proporcional à vida útil restante do pneu:

```
volta_pit_stop = round(voltas_totais * (100 - desgaste_pneu) / 100)
```

saturada em no mínimo a volta 1. As **voltas restantes** usadas na regra 3
são `voltas_totais - volta_pit_stop`.

### Retorno

```python
{
    "volta_pit_stop": int,      # volta sugerida para o pit stop
    "composto": str,            # "Wet" | "Soft" | "Hard"
    "alerta_emergencia": bool,  # True se desgaste >= 80%
    "mensagem": str,            # texto para o painel da telemetria
}
```

---

## 2. Como executar

```bash
# 1. ambiente virtual (opcional, recomendado)
python -m venv .venv
source .venv/bin/activate      # Windows: .venv\Scripts\activate

# 2. dependências
pip install -r requirements.txt

# 3. suíte de testes
pytest

# 4. cobertura no terminal (linhas não cobertas em destaque)
pytest --cov=estrategia_f1 --cov-report=term-missing

# 5. relatório visual em HTML -> abrir htmlcov/index.html
pytest --cov=estrategia_f1 --cov-report=html
```

A cobertura de **ramos** (`branch coverage`) está habilitada no
`.coveragerc`, portanto os comandos acima já reportam instruções **e** ramos.

---

## 3. Estrutura do repositório

```
.
├── estrategia_f1.py        # módulo sob teste (versão corrigida)
├── test_estrategia.py      # suíte pytest (caixa preta + caixa branca)
├── requirements.txt        # pytest e pytest-cov
├── pytest.ini              # configuração do pytest
├── .coveragerc             # cobertura de instruções e de ramos
├── MAPEAMENTO_TESTES.md    # mapeamento CP## -> regra de negócio
└── RELATORIO_BUGS.md       # os 3 bugs encontrados e suas correções
```

---

## 4. Documentos do trabalho

- [`MAPEAMENTO_TESTES.md`](MAPEAMENTO_TESTES.md), da Etapa 1, com os casos de
  teste de caixa preta e sua rastreabilidade com as regras de negócio.
- [`RELATORIO_BUGS.md`](RELATORIO_BUGS.md), da Etapa 3, com os 3 bugs
  identificados no código base, os testes que os revelaram e as correções
  aplicadas.

O histórico de commits deste repositório registra a evolução exigida pelo
enunciado: código base com os defeitos, suíte de testes que os expõe,
correções uma a uma e fechamento da cobertura em 100%.

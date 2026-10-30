# Auditoria de Acessibilidade Web: Portal TechInclusiva

Trabalho Prático II da Unidade Curricular **Qualidade e Teste de Software** (UniSENAI),
orientado pelo Prof. MSc. Hugo Menezes Barra.

**Integrantes:** Luiz Gustavo Mella Massing e João Victor Bagatim

O projeto audita um portal de notícias fictício contra a **WCAG 2.1 níveis A e AA**.
O axe-core CLI roda no terminal do VSCode e gera um JSON. O script
`parser_acessibilidade.py` lê esse JSON, prioriza as violações por severidade, mapeia
cada uma para o critério WCAG correspondente e exporta um relatório em PDF.

## Resultado

| Página | Regras violadas | Elementos afetados | critical | serious | moderate | minor |
|---|---|---|---|---|---|---|
| [`index.html`](index.html) (original) | 17 | 27 | 5 | 9 | 2 | 1 |
| [`index_corrigido.html`](index_corrigido.html) | 0 | 0 | 0 | 0 | 0 | 0 |

Relatórios gerados pelo parser:
- [relatorios/relatorio_resultado_axe.pdf](relatorios/relatorio_resultado_axe.pdf): diagnóstico da página original
- [relatorios/relatorio_resultado_axe_corrigido.pdf](relatorios/relatorio_resultado_axe_corrigido.pdf): reteste da página corrigida

## Comprovação da execução no VSCode

O `resultado_axe.json` deste repositório foi gerado pelo comando `npm run axe`, executado no
terminal integrado do VSCode. Os prints da execução completa estão em
[evidencias/](evidencias/):

- [print_terminal_vscode_1_inicio.png](evidencias/print_terminal_vscode_1_inicio.png): comando e primeiras violações
- [print_terminal_vscode_2_meio.png](evidencias/print_terminal_vscode_2_meio.png): continuação da lista de violações
- [print_terminal_vscode_3_fim.png](evidencias/print_terminal_vscode_3_fim.png): total de 27 ocorrências e arquivo salvo

## Estrutura

| Arquivo | Conteúdo |
|---|---|
| `index.html` | Portal TechInclusiva com barreiras de acessibilidade propositais |
| `index_corrigido.html` | Mesmo portal com todas as violações corrigidas |
| `resultado_axe.json` | Saída bruta do axe CLI para `index.html` (entregável 2) |
| `resultado_axe_corrigido.json` | Saída do axe CLI para a página corrigida |
| `parser_acessibilidade.py` | Script Python documentado que analisa o JSON (entregável 3) |
| `tests/` | Testes unitários do parser com pytest |
| `relatorios/` | PDFs gerados pelo parser |
| `evidencias/` | Prints e logs da execução do axe no terminal do VSCode, saídas do parser e capturas das páginas |
| `.vscode/` | Extensões recomendadas e tarefas para rodar tudo pelo VSCode |
| `PLANEJAMENTO.md` | Critérios de aceite e decisões tomadas |

## Pré-requisitos

- Node.js 18 ou superior
- Python 3.10 ou superior
- Google Chrome. O `package.json` fixa o `chromedriver` na versão 155, a mesma do
  Chrome usado na auditoria. Com outra versão do Chrome, rode
  `npm install chromedriver@<versão-do-chrome>`.

## Como executar (terminal do VSCode)

```bash
# 1. Instalar as dependências
npm install
python -m venv .venv
.venv/Scripts/pip install -r requirements.txt     # Windows (no Linux/macOS: .venv/bin/pip)

# 2. Servir o portal (deixe este terminal aberto)
python -m http.server 8080

# 3. Em outro terminal, rodar a varredura do axe
npm run axe                 # gera resultado_axe.json
npm run axe:corrigido       # gera resultado_axe_corrigido.json

# 4. Analisar o JSON e gerar o PDF
.venv/Scripts/python parser_acessibilidade.py resultado_axe.json
.venv/Scripts/python parser_acessibilidade.py resultado_axe_corrigido.json

# 5. Rodar os testes do parser
.venv/Scripts/python -m pytest -v
```

Os mesmos passos estão em **Terminal > Run Task** (arquivo `.vscode/tasks.json`).

O comando `npm run axe` executa:

```bash
axe http://localhost:8080/index.html --tags wcag2a,wcag2aa,wcag21a,wcag21aa \
    --chromedriver-path node_modules/chromedriver/lib/chromedriver/chromedriver.exe \
    --save resultado_axe.json --dir . --show-errors
```

As tags limitam a varredura às regras da WCAG 2.0 e 2.1, níveis A e AA. Em Linux ou
macOS, troque `chromedriver.exe` por `chromedriver` no `package.json`.

## O que o parser faz

| Requisito do enunciado | Implementação |
|---|---|
| 1. Leitura de dados | `carregar_relatorio()` aceita a lista do axe CLI e o objeto da API do axe-core. Arquivo inexistente ou JSON inválido geram uma mensagem clara e código de saída 2 |
| 2. Extração de falhas | `extrair_violacoes()` lê a chave `violations` |
| 3. Categorização | `categorizar_por_severidade()` agrupa na ordem critical > serious > moderate > minor |
| 4. Mapeamento de elementos | Cada violação lista o seletor CSS (`target`) e o trecho HTML (`html`) de cada elemento afetado |
| 5. Relatório final | `imprimir_resumo()` mostra o resumo no terminal e `gerar_pdf()` salva o PDF em `relatorios/` |
| Mapeamento WCAG | `mapear_criterios_wcag()` converte tags como `wcag143` no critério 1.4.3 Contraste (Mínimo), nível AA |

Uso: `python parser_acessibilidade.py [arquivo.json] [--pdf saida.pdf]`.
Sem argumentos, o script lê `resultado_axe.json` e grava `relatorios/relatorio_resultado_axe.pdf`.

## Decisões

- O enunciado não trazia o código do `index.html`. O grupo criou um portal fictício cujas
  barreiras geram violações nos quatro níveis de severidade.
- A ferramenta escolhida foi o axe-core CLI, porque o JSON dele já traz o campo `impact`
  com as quatro severidades pedidas.
- O enunciado chama o script de `parser_acessibilidade.py` (seção 4) e de `parser.py`
  (seção 5). Usamos o primeiro nome.
- Testes automatizados cobrem apenas parte das barreiras de acessibilidade (a Deque estima
  entre 20% e 50%). Navegação por teclado, leitores de tela e clareza do conteúdo exigem
  avaliação manual complementar.

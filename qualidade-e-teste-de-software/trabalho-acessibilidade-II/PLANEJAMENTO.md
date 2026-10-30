# Planejamento e Critérios de Aceite

Trabalho Prático II: Teste de Auditoria de Acessibilidade Web
Unidade Curricular: Qualidade e Teste de Software (Prof. MSc. Hugo Menezes Barra)
Integrantes: Luiz Gustavo Mella Massing e João Victor Bagatim

## Decisões tomadas com o grupo

| Ponto ambíguo do enunciado | Decisão |
|---|---|
| A seção 3 do PDF não traz o código do `index.html` | Criamos um portal fictício "TechInclusiva" com barreiras propositais |
| Ferramenta: axe CLI ou Lighthouse CLI | axe-core CLI (`@axe-core/cli`), porque o JSON já traz o campo `impact` |
| Nome do script: `parser_acessibilidade.py` (seção 4) ou `parser.py` (seção 5) | `parser_acessibilidade.py`. Ele é o entregável "Script Python (parser.py)" |
| O PDF do script e o relatório final | O PDF gerado pelo script fica no repositório. O relatório final (diagnóstico, HTML corrigido e link) é entregue direto no AVA |
| O chromedriver 152 do axe CLI não funciona com o Chrome 155 | Instalamos `chromedriver@155` à parte e passamos `--chromedriver-path` |

## Critérios de aceite

### Etapa 0: Setup do ambiente no VSCode
- [x] `package.json` com `@axe-core/cli` e `chromedriver` como devDependencies e scripts npm
- [x] `requirements.txt` e `.venv` com as dependências do parser
- [x] `.vscode/extensions.json` recomendando Live Server e axe Accessibility Linter
- [x] `.vscode/tasks.json` para servir, varrer e parsear pelo VSCode

### Etapa 1: HTML de teste (`index.html`)
- [x] Portal TechInclusiva servido em `http://localhost:8080`
- [x] Barreiras que produzem violações nos quatro níveis: critical, serious, moderate e minor
- [x] Barreiras ligadas a critérios WCAG 2.1 de nível A e AA

### Etapa 2: Varredura automatizada
- [x] axe CLI executado no terminal do VSCode com as tags `wcag2a,wcag2aa,wcag21a,wcag21aa`
- [x] Saída salva em `resultado_axe.json`
- [x] Log da execução salvo em `evidencias/` (comando, versões e data)
- [x] Prints do terminal do VSCode em `evidencias/print_terminal_vscode_*.png`

### Etapa 3: Script Python (`parser_acessibilidade.py`)
- [x] 1. Leitura: carrega o JSON informado por argumento e trata arquivo inexistente ou inválido
- [x] 2. Extração: obtém a lista de `violations`
- [x] 3. Categorização: agrupa por severidade, na ordem critical > serious > moderate > minor
- [x] 4. Mapeamento: lista seletores CSS e o trecho HTML de cada elemento afetado
- [x] Extra: mapeia cada regra para o critério de sucesso WCAG 2.1 (número, nome e nível)
- [x] 5. Relatório: resumo consolidado no terminal e PDF salvo no repositório
- [x] Código documentado (docstrings e README de uso)
- [x] Extra de QA: testes unitários com pytest

### Etapa 4: Correção e reteste
- [x] `index_corrigido.html` resolve todas as violações
- [x] Nova varredura gera `resultado_axe_corrigido.json` com zero violações

### Etapa 5: Relatório final (PDF entregue no AVA, no padrão do PDF de referência)
- [x] Diagnóstico por severidade, regra, critério WCAG, elementos e remediação
- [x] Código HTML corrigido (trechos antes e depois)
- [x] Evidências de execução e link do GitHub

### Etapa 6: Publicação
- [ ] Commit e push em `LuizGustavoMM/UniSenai`
- [ ] Entrega no AVA (feita pelo grupo)

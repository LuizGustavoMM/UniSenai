# Teste de Carga e Estresse em E-sports com Locust

Trabalho de Teste de Stress da Unidade Curricular **Qualidade e Teste de Software**
(UniSENAI), orientado pelo Prof. MSc. Hugo Menezes Barra.

**Aluno:** Luiz Gustavo Mella Massing

No cenário do trabalho, a desenvolvedora RiotForge precisa validar a API do placar ao vivo
e dos perfis de jogadores do seu campeonato mundial. A parte prática simula os torcedores
consultando os perfis dos jogadores profissionais, papel feito pela rota `/users` da API
pública [JSONPlaceholder](https://jsonplaceholder.typicode.com).

| | |
|---|---|
| **Script** | [`locustfile_esports.py`](locustfile_esports.py) |
| **Ferramenta** | Python 3.11 · Locust 2.47.0 |
| **Host** | `https://jsonplaceholder.typicode.com` |
| **Comportamento** | `GET /users` com `wait_time` entre 2 e 4 segundos |

## O script

Cada usuário virtual (`JogadorEsports`, subclasse de `HttpUser`) faz um `GET /users` e espera
um tempo sorteado entre 2 e 4 segundos antes de repetir. A resposta é validada com
`catch_response=True`: além do status 200, o script confere se o corpo é um JSON válido e se
a lista de perfis não veio vazia. Qualquer desvio é registrado como falha no Locust.

## Resultados

Seis execuções feitas pelo painel web do Locust em 7 de novembro de 2026. O teste base
segue os parâmetros do enunciado (50 usuários, 5 por segundo, 3 minutos). Os outros cinco
variam a carga para mais e para menos e a duração.

| Teste | Usuários | Spawn rate | Duração | Requisições | Falhas | Req/s | p50 (ms) | p95 (ms) | p99 (ms) | Máx (ms) |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| [T1](resultados/teste_1_10u) | 10 | 1/s | 2 min | 373 | 0 | 3,1 | 130 | 140 | 390 | 528 |
| [T2](resultados/teste_2_25u) | 25 | 3/s | 3 min | 1.414 | 0 | 7,8 | 130 | 140 | 400 | 438 |
| [**Base**](resultados/teste_0_base_50u) | **50** | **5/s** | **3 min** | **2.806** | **0** | **15,6** | **130** | **140** | **410** | **681** |
| [T3](resultados/teste_3_100u) | 100 | 10/s | 3 min | 5.623 | 0 | 31,2 | 130 | 140 | 400 | 656 |
| [T4](resultados/teste_4_200u) | 200 | 20/s | 5 min | 18.900 | 0 | 63,0 | 130 | 140 | 410 | 987 |
| [T5](resultados/teste_5_500u) | 500 | 50/s | 3 min | 28.156 | 0 | 156,2 | 130 | 140 | 460 | 742 |

Em resumo:

- **Nenhuma falha** nas 57.272 requisições.
- **A latência não muda com a carga.** O p95 ficou em 140 ms tanto com 10 quanto com 500 usuários.
- **A vazão cresce na mesma proporção dos usuários**, entre 97% e 99% do valor teórico
  `usuários ÷ (3 s de espera + 0,14 s de resposta)`. Quem limita a vazão é o tempo de espera dos
  usuários, não o servidor. O ponto de colapso não foi alcançado.
- **O pico de ~400 ms no começo de cada teste é o handshake TCP + TLS** de cada usuário novo, e não
  sobrecarga. O [diagnóstico de rede](resultados/diagnostico_rede.txt) mostra ping de 126 ms e
  ~420 ms para uma requisição em conexão nova.
- **A resposta vem do cache do Cloudflare em São Paulo** (`cf-cache-status: HIT`, `CF-RAY ...-GRU`).
  Os ~130 ms medidos são praticamente só o tempo de rede.

A análise completa e as respostas da parte teórica estão no relatório em PDF entregue no AVA.

### Evidências

Cada pasta em [`resultados/`](resultados) contém:

| Arquivo | Conteúdo |
|---|---|
| `01_configuracao.png` | Formulário do painel preenchido antes do início |
| `02_charts_durante.png` | Aba Charts na metade da execução |
| `03_statistics_final.png` | Aba Statistics ao final |
| `04_charts_final.png` | Aba Charts ao final da execução |
| `05_failures.png` | Aba Failures (vazia em todos os testes) |
| `relatorio_locust.html` | Relatório HTML exportado pelo painel (aba Download Data) |
| `stats.csv`, `failures.csv`, `stats.json` | Estatísticas exportadas pelo painel |
| `parametros.json` | Usuários, spawn rate, duração e horário de início |
| `terminal.log` | Saída do Locust no terminal |

## Como executar

```bash
# 1. ambiente virtual
python -m venv .venv
.venv\Scripts\activate          # Linux/macOS: source .venv/bin/activate

# 2. dependências
pip install -r requirements.txt

# 3. subir o Locust
python -m locust -f locustfile_esports.py
```

Depois abrir `http://localhost:8089` e informar:

- **Number of users:** 50
- **Ramp up:** 5
- **Host:** `https://jsonplaceholder.typicode.com`
- **Advanced options > Run time:** `3m`

> O JSONPlaceholder é um serviço público e gratuito. Os testes foram limitados a 500 usuários
> para não sobrecarregar um serviço usado por outras pessoas.

## Estrutura

```
.
├── locustfile_esports.py   # usuário virtual do Locust
├── requirements.txt        # locust
└── resultados/
    ├── diagnostico_rede.txt
    ├── teste_0_base_50u/   # teste pedido no enunciado
    ├── teste_1_10u/
    ├── teste_2_25u/
    ├── teste_3_100u/
    ├── teste_4_200u/
    └── teste_5_500u/
```

# Mesa D&D — Servidor + Painel do Mestre

Projeto da disciplina DSMD. Sistema para gerenciar **mesas de RPG**: o **mestre** cria mesas no navegador do notebook e vê as **fichas** de todos os jogadores; os **jogadores** (APK Android) entram em uma mesa pelo **código** e enviam suas fichas.

> Este README documenta **o que já está implementado**, não o projeto original. A arquitetura anterior usava Supabase; **isso foi abandonado** — hoje o backend é uma **API própria em Dart (shelf)** com **PostgreSQL local**, sem Supabase.

## Componentes (Docker Compose)

| Serviço | O que é | Tecnologia | Porta no host |
|---|---|---|---|
| `db` | Banco de dados do servidor | PostgreSQL 16 | `5432` |
| `servidor` | API REST (JSON) | Dart + `shelf` / `shelf_router` + `postgres` | `8000` → 8080 no contêiner |
| `web-mestre` | Painel do mestre | Flutter Web + `flutter_bloc` (Cubit) + `http`, servido por nginx | `8080` → 80 no contêiner |

O **APK do jogador** (`app-jogador/`) **não** faz parte do Docker Compose — é um app Android instalado no celular, que fala com a API pela rede.

```
trabalho-flutter/
├── docker-compose.yml        # sobe db + servidor + web-mestre
├── servidor/                 # API Dart (shelf)
│   ├── bin/server.dart       # entrypoint (pipeline: log + CORS + erros + rotas)
│   ├── lib/src/
│   │   ├── db.dart           # acesso ao PostgreSQL (pool, migração, queries)
│   │   ├── middleware.dart   # CORS, tratamento de erros, auth por código no header
│   │   └── router.dart       # rotas do mestre e do jogador
│   └── Dockerfile            # compila para executável nativo (imagem `scratch`)
├── web-mestre/               # painel Flutter Web
│   ├── lib/
│   │   ├── data/             # api_config, models, repositories (http + cache no navegador)
│   │   ├── logic/            # mesa_cubit, ficha_cubit (+ states)
│   │   └── presentation/     # home_shell (NavigationRail) + screens
│   ├── Dockerfile            # build Flutter Web → nginx
│   └── nginx.conf            # fallback SPA
└── app-jogador/              # APK Android do jogador (Flutter)
    ├── lib/
    │   ├── data/
    │   │   ├── local/        # database_helper (sqflite singleton)
    │   │   ├── models/       # jogador, mesa, ficha
    │   │   └── repositories/ # jogador (uuid), mesa (validar código), ficha (local + sync)
    │   ├── logic/            # jogador_cubit, mesa_cubit, ficha_cubit (+ states)
    │   └── presentation/screens/  # identidade, minhas_mesas, entrar_mesa, ficha
    └── android/              # manifest com INTERNET + usesCleartextTraffic
```

## Como rodar

Pré-requisito: Docker Desktop.

```bash
docker compose up --build      # sobe os três serviços
# Painel do mestre:  http://localhost:8080
# API (Postman):     http://localhost:8000
docker compose down            # parar (some com os dados: use "down -v" para apagar o volume)
```

## Modelo de dados (PostgreSQL)

As tabelas são criadas automaticamente pelo servidor no startup (`CREATE TABLE IF NOT EXISTS`).

- **`mesas`** — `id` (UUID), `codigo` (TEXT único), `nome`, `created_at`.
- **`fichas`** — `id` (UUID), `mesa_id` (FK → mesas), `jogador_id`, `nome_personagem`, `versao` (int), `dados` (JSONB opaco), `updated_at`. Única por `(mesa_id, jogador_id)`.

> Decisão: **uma tabela `fichas` com `mesa_id`** (em vez de uma tabela física por mesa) — isola as fichas por mesa de forma simples e escalável.

## API

Base: `http://localhost:8000`. Corpo e respostas em JSON.

### Rotas do mestre (navegador em localhost — sem código)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/mesas` | Cria mesa. Corpo `{"nome":"..."}`. Retorna `201` com `{id, codigo, nome, created_at}` (o servidor **gera o código** e o **id**). |
| `GET` | `/mesas` | Lista todas as mesas. |
| `GET` | `/mesas/<id>` | Detalhe de uma mesa. |
| `GET` | `/mesas/<id>/fichas` | Todas as fichas da mesa (visão do mestre). |
| `DELETE` | `/mesas/<id>` | Remove a mesa (e suas fichas em cascata). |

### Rotas do jogador (APK) — exigem o header `X-Mesa-Codigo`

A "autenticação" do jogador é o **código da mesa no header** `X-Mesa-Codigo` (o mesmo que o mestre mostra na tela). Sem o header → `401`; código inválido → `403`.

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/mesa/info` | Valida o código e retorna a mesa. |
| `GET` | `/mesa/fichas` | Fichas da mesa (aceita `?jogador_id=`). |
| `GET` | `/mesa/fichas/<jogadorId>` | Ficha de um jogador (para checar versão). |
| `POST` | `/mesa/fichas` | Cria/sincroniza a ficha. Corpo `{jogador_id, nome_personagem, versao, dados}`. |

**Sincronização ("maior versão vence")** no `POST /mesa/fichas`:
- ficha não existe → **cria** (`201`, `status: criada`);
- `versao` recebida **>** a do servidor → **atualiza** (`200`, `status: atualizada`);
- `versao` **<=** a do servidor → **nada a fazer** (`200`, `status: inalterada`).

### Exemplos (curl / Postman)

```bash
# Mestre cria a mesa
curl -X POST http://localhost:8000/mesas \
  -H 'Content-Type: application/json' -d '{"nome":"Tumba de Horrores"}'
# -> {"id":"...","codigo":"XI84OR","nome":"Tumba de Horrores",...}

# Jogador entra usando o código no header
curl http://localhost:8000/mesa/info -H 'X-Mesa-Codigo: XI84OR'

# Jogador envia a ficha (JSON livre em "dados")
curl -X POST http://localhost:8000/mesa/fichas \
  -H 'X-Mesa-Codigo: XI84OR' -H 'Content-Type: application/json' \
  -d '{"jogador_id":"jog-abc","nome_personagem":"Aragorn","versao":1,"dados":{"raca":"Humano","classe":"Guardiao"}}'

# Mestre lista as fichas da mesa
curl http://localhost:8000/mesas/<ID_DA_MESA>/fichas
```

## Painel do mestre (Flutter Web)

- **Barra lateral recolhível** (`NavigationRail`): ícone hambúrguer (3 linhas) abre/fecha; recolhida mostra só ícones, aberta mostra os rótulos. Abas **"Nova mesa"** e **"Minhas mesas"** (ícone de mapa).
- **Nova mesa**: campo com o nome → botão **"Criar mesa"** → chama `POST /mesas` e exibe o **código gerado** em destaque (com botão copiar).
- **Minhas mesas**: lista as mesas do **cache do navegador** (`shared_preferences` → localStorage). Como não há login, o cache é a "memória" do mestre. Tocar em uma mesa abre o painel.
- **Painel da mesa**: mostra o código e busca no servidor (`GET /mesas/<id>/fichas`) **todas as fichas** dos jogadores; cada ficha é expansível e exibe o JSON.

Arquitetura em camadas `data / logic / presentation` com Cubit (`flutter_bloc`) e `http` — mesmo padrão das aulas.

## APK do jogador (Flutter Android)

App instalado no celular do jogador (`app-jogador/`). Banco local em **sqflite** (`DatabaseHelper` singleton), estado com Cubit, comunicação por `http`.

Fluxo:
1. **Identidade local (sem login)** — na primeira execução o app gera um **UUID** e pede um **apelido** (guardados no sqflite). O UUID é o `jogador_id` enviado ao servidor.
2. **Entrar em mesa** — informa o **endereço do servidor** (IP do notebook, ex.: `http://192.168.137.1:8000`) e o **código** da mesa. O app valida em `GET /mesa/info` (header `X-Mesa-Codigo`) e salva a mesa localmente.
3. **Minhas mesas** — lista as mesas locais (funciona offline). Uma ficha por mesa.
4. **Ficha** — campo "Nome do personagem" + uma **caixa de texto livre** (raça, subclasse, equipamentos etc.). Ao salvar: grava no sqflite incrementando a `versao` e marcando pendente; depois faz `POST /mesa/fichas`. O servidor decide ("maior versão vence"); em caso de sucesso a ficha vira "sincronizada". Sem rede, fica pendente e reenvia no próximo save.

### Build do APK

Sem Flutter instalado localmente, compila-se via Docker (mesma imagem do build web):

```bash
docker run --rm -v "$(pwd)/app-jogador:/app" -w /app \
  ghcr.io/cirruslabs/flutter:stable \
  bash -c "flutter clean && flutter pub get && flutter build apk --release"
# APK gerado em: app-jogador/build/app/outputs/flutter-apk/app-release.apk
```
> Use `flutter clean` no mesmo comando: rodar `analyze` e `build` em contêineres separados sobre o mesmo volume pode deixar artefatos inconsistentes e quebrar o build (erro `'Matrix4' isn't a type`).

Instalar no aparelho (testado com Xiaomi Mi 13, Android 14): copiar o `.apk` para o celular e instalar habilitando "instalar de fontes desconhecidas". O manifest declara `INTERNET` e `usesCleartextTraffic="true"` (a API é `http://` na rede local).

## Rede (uso em mesa real)

O mestre usa o painel em `http://localhost:8080`. Os celulares dos jogadores acessam a **API** pelo IP do notebook na rede local (ex.: hotspot do Windows `192.168.137.1`), em `http://IP_DO_NOTEBOOK:8000`. O mestre informa aos jogadores esse IP e o **código** da mesa.

## Testar localmente sem o celular

Como a "autenticação" do jogador é só o código no header, dá para **simular o jogador por HTTP** (Postman/curl) sem o APK — testa exatamente o mesmo contrato que o app usa:

```bash
docker compose up -d
# 1) Mestre cria a mesa (ou crie pelo painel) e pegue o código:
CODIGO=$(curl -s -X POST http://localhost:8000/mesas -H 'Content-Type: application/json' \
  -d '{"nome":"Mesa de Teste"}' | grep -o '"codigo":"[^"]*"' | cut -d'"' -f4)
# 2) Jogador entra e envia a ficha (como o APK faria):
curl -s http://localhost:8000/mesa/info -H "X-Mesa-Codigo: $CODIGO"
curl -s -X POST http://localhost:8000/mesa/fichas -H "X-Mesa-Codigo: $CODIGO" \
  -H 'Content-Type: application/json' \
  --data-binary '{"jogador_id":"jog-1","nome_personagem":"Aragorn","versao":1,"dados":{"conteudo":"Raca: Humano\nClasse: Guardiao"}}'
```
Depois abra o painel em `http://localhost:8080`, entre na mesa e atualize — a ficha aparece.
(Para clicar na UI real do jogador sem device, seria preciso adicionar suporte **web** ao `app-jogador` — trocando o sqflite pela versão web. Ainda não feito; ver To-Do.)

## Requisitos

### Funcionais

| # | Requisito | Status |
|---|---|---|
| RF01 | Mestre cria uma mesa com nome; o sistema gera **código único** + **id interno** | ✅ |
| RF02 | Mestre vê a lista das suas mesas (cache do navegador) | ✅ |
| RF03 | Mestre abre uma mesa e vê **todas as fichas** dos jogadores | ✅ |
| RF04 | Jogador tem identidade local (UUID + apelido), **sem login** | ✅ |
| RF05 | Jogador entra na mesa por **IP do servidor + código** | ✅ |
| RF06 | Jogador cria/edita a ficha (nome + texto livre) | ✅ |
| RF07 | Ficha salva localmente com **versão incremental** (sqflite) | ✅ |
| RF08 | Sincronização **"maior versão vence"** com o servidor | ✅ |
| RF09 | Jogador participa de **várias mesas**, uma ficha por mesa | ✅ |
| RF10 | API testável por Postman (código no header `X-Mesa-Codigo`) | ✅ |
| RF11 | Ficha **estruturada** (campos de RPG) em vez de texto livre | ⏳ |
| RF12 | Encerrar/remover mesa pela UI do mestre (API já tem `DELETE`) | ⏳ |
| RF13 | "Testar conexão" e editar o endereço do servidor no app | ⏳ |
| RF14 | Atualização automática/pull-to-refresh das fichas no painel | ⏳ |

### Não funcionais

| # | Requisito | Status |
|---|---|---|
| RNF01 | Funciona em **rede local sem internet** | ✅ |
| RNF02 | App do celular **offline-first** (sqflite; salva sem rede) | ✅ |
| RNF03 | Empacotamento em **Docker Compose** (db + servidor + web) | ✅ |
| RNF04 | Tecnologias das aulas (Flutter, Cubit, `http`, `sqflite`) + `shelf`/`postgres` | ✅ |
| RNF05 | APK roda em **Android 14** (Xiaomi Mi 13) | ✅ |
| RNF06 | Arquitetura em camadas `data / logic / presentation` | ✅ |
| RNF07 | API com CORS e erros padronizados em JSON | ✅ |
| RNF08 | Endereço da API configurável no build web (`--dart-define API_URL`) | ✅ |
| RNF09 | HTTPS / segurança de transporte | ⏳ |
| RNF10 | Testes automatizados (unit/integração) | ⏳ |

## To-Do (próximas implementações)

- [ ] **Ficha estruturada** — substituir o texto livre por campos de RPG (raça, classe, atributos, equipamentos…); o servidor continua tratando `dados` como JSON opaco (RF11).
- [ ] **Encerrar mesa no painel** do mestre, usando `DELETE /mesas/<id>` (RF12).
- [ ] **App do jogador:** botão "testar conexão" (GET `/`), editar o endereço do servidor depois de entrar, e exibir UUID/apelido (RF13).
- [ ] **Atualização das fichas** no painel: pull-to-refresh / polling automático (RF14).
- [ ] **Reenvio automático** de fichas pendentes (`sincronizada = 0`) ao reconectar, sem depender de novo save.
- [ ] **Suporte web no `app-jogador`** para testar a UI sem device (trocar sqflite por `sqflite_common_ffi_web`).
- [ ] **Autenticação** de verdade (evolução; hoje é só o código da mesa).
- [ ] **HTTPS** e/ou validação de origem (RNF09).
- [ ] **Testes automatizados** do servidor (rotas + regra de versão) e dos repositórios (RNF10).
- [ ] **Melhorar diagnóstico de rede** no app (mensagens claras para firewall/isolamento de clientes).

## Estado do projeto

- ✅ Servidor (API + PostgreSQL) em Docker.
- ✅ Painel do mestre (criar mesa, listar mesas do cache, ver fichas).
- ✅ APK do jogador — versão inicial (identidade local, entrar por código, criar ficha em texto livre, salvar no sqflite e sincronizar por versão).

# Apanhado geral — Tecnologias e padrões usados pelo professor em aula

Inventário completo extraído da leitura de todos os arquivos das pastas de aula (11/06, 18/06, 18/06-API, 25/06-Autenticação e "codigo sqlite").

---

## 1. Resumo por aula

### Aula 11/06 — Fundamentos e primeira gestão de estado

- **Flutter/Dart** com `MaterialApp`, `ThemeData` + `ColorScheme.fromSeed`, `Scaffold`, `AppBar`, `ListView.builder`, `ListTile`, `Checkbox`, `IconButton`, `CircularProgressIndicator`.
- **StatefulWidget + `setState`**: primeira forma de gestão de estado (tela `task_screen.dart`).
- **Arquitetura em camadas** já apresentada desde o início: `data/models`, `data/repositories`, `logic`, `presentation/screens`.
- **Padrão Repository** com dados *mock* e `Future.delayed` para simular latência (assincronismo com `async/await`).
- **Cubit** (`flutter_bloc`) introduzido: `TaskCubit` com o ciclo de estados `TaskInitial → TaskLoading → TaskLoaded / TaskError` e `emit()`.
- **Model imutável** com método de cópia (`copyTaskModel`, padrão *copyWith*).

### Aula 18/06 — Cubit consolidado e injeção de dependência

- **`RepositoryProvider` + `BlocProvider`** no `main.dart` (injeção de dependência via `context.read<T>()`).
- **`BlocBuilder`** na tela: UI reage aos estados do Cubit (`TaskScreenCubit` como `StatelessWidget`).
- Comparação didática mantida no projeto: `task_screen.dart` (setState) vs `task_screen_cubit.dart` (Cubit).
- **`FloatingActionButton` + `AlertDialog` + `TextEditingController`** para entrada de dados (criar tarefa).

### Aula 18/06 — API (consumo REST com Supabase)

- **Pacote `http`** + **`dart:convert`** (`jsonEncode` / `jsonDecode`).
- **Supabase como backend (BaaS)** consumido por **REST puro** — sem SDK: endpoint **PostgREST** `.../rest/v1/<tabela>`.
- **Headers padrão**: `apikey`, `Authorization: Bearer <key>`, `Content-Type: application/json`, `Prefer: return=minimal` no POST.
- **Serialização JSON no model**: `factory fromJson(Map)` e `toJson()`.
- Tratamento de **status codes HTTP** (200, 201, 204) e `Exception` com try/catch; erro vira estado `TaskError` exibido na tela.

### Aula 25/06 — Autenticação

- **Supabase Auth (GoTrue)** via REST: `POST /auth/v1/token?grant_type=password` com e-mail/senha.
- **JWT**: `AuthModel` guarda `access_token` e `userId`.
- **`AuthCubit` / `AuthState`** (`AuthInitial / AuthLoading / AuthSuccess / AuthError`).
- **`BlocConsumer`** (listener + builder): navegação no sucesso, `SnackBar` no erro.
- **Rotas nomeadas**: `initialRoute`, `routes`, `Navigator.pushReplacementNamed` com `arguments`, leitura via `ModalRoute.of(context)`.
- **`MultiRepositoryProvider` / `MultiBlocProvider`** para registrar vários repositórios/cubits.
- **CRUD completo via REST** no `TaskRepositorySupabase`: GET, POST, **PATCH** (`?id=eq.<id>`, sintaxe de filtro do PostgREST) e **DELETE**.

### "codigo sqlite" — Banco de dados local e modo offline

- **`sqflite` + `path`**: banco SQLite local no dispositivo (`getDatabasesPath`, `join`, `openDatabase`, `onCreate` com `CREATE TABLE`).
- **`DatabaseHelper` singleton** — instância única de conexão para evitar corrupção do arquivo.
- **CRUD sqflite**: `db.query`, `db.insert`, `db.delete`.
- **Padrão de sincronização/cache offline** (o mais importante para o projeto):
  1. `getTasks()` busca primeiro **na nuvem** (Supabase via REST);
  2. em caso de sucesso, **espelha os dados no SQLite local** (limpa a tabela e regrava);
  3. em caso de falha de rede, **cai no modo offline** e lê os dados do SQLite.

---

## 2. Inventário consolidado

| Categoria | Tecnologia / padrão | Onde apareceu |
|---|---|---|
| Linguagem/Framework | Dart + Flutter (Material Design) | Todas as aulas |
| Gestão de estado | `setState` (didático) → **Cubit / `flutter_bloc`** | 11/06 em diante |
| Estados padrão | `Initial / Loading / Loaded(Success) / Error` | Todos os cubits |
| Injeção de dependência | `RepositoryProvider`, `BlocProvider` e variantes `Multi*` | 18/06, 25/06 |
| Reatividade na UI | `BlocBuilder`, `BlocConsumer`, `context.read<T>()` | 18/06, 25/06 |
| Arquitetura | Camadas `data / logic / presentation` + padrão Repository | Todas as aulas |
| Models | Imutáveis, `copyWith`, `fromJson` / `toJson` | Todas as aulas |
| HTTP | Pacote `http` + `dart:convert` (JSON) | 18/06-API, 25/06 |
| Backend (BaaS) | **Supabase**: PostgREST (`/rest/v1`) + filtros `?col=eq.valor` | 18/06-API, 25/06 |
| Autenticação | **Supabase Auth / GoTrue** (`/auth/v1/token`), JWT | 25/06 |
| Banco local | **`sqflite` + `path`**, `DatabaseHelper` singleton, SQL DDL/CRUD | codigo sqlite |
| Offline | Cache local espelhado + fallback quando sem rede | codigo sqlite |
| Navegação | Rotas nomeadas + `arguments` | 25/06 |
| UI diversos | `ListView.builder`, `AlertDialog`, `SnackBar`, `FloatingActionButton` | Várias |

**Pacotes pub.dev usados em aula:** `flutter_bloc`, `http`, `sqflite`, `path`.

---

## 3. Mapeamento direto para o projeto Mesa D&D

| Necessidade do projeto | Tecnologia da aula que resolve |
|---|---|
| App do jogador (APK Android) | Flutter + camadas + Cubit |
| Página do mestre no navegador | Flutter compilado para **web** (mesmo código/padrões) |
| Banco no servidor (Docker no notebook) | **Supabase self-hosted** — mesma API REST/Auth usada em aula, rodando em contêineres Docker |
| Comunicação app ↔ servidor | `http` + JSON + endpoints PostgREST (idêntico às aulas) |
| Identificação de mestre e jogadores | **Sem autenticação no escopo atual**: código gerado ao criar a sessão + UUID gerado no app do jogador. O Supabase Auth (aula 25/06) fica documentado como estudado e disponível para evolução futura |
| Banco local da ficha no celular | `sqflite` + `DatabaseHelper` singleton (codigo sqlite) |
| Sincronização de fichas | Extensão do padrão offline do professor: em aula, nuvem→local; no projeto, acrescenta-se **local→servidor com controle de versão** |

> **Única adição fora do material de aula:** o Docker (exigência do projeto para rodar o servidor no notebook). A escolha do Supabase self-hosted minimiza esse desvio: é o mesmo Supabase das aulas, apenas hospedado localmente — a distribuição oficial já é entregue como Docker Compose.

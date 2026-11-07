"""
Teste de carga e estresse da API de perfis de jogadores (cenario RiotForge).

Cada usuario virtual representa um torcedor que abre o perfil dos jogadores
profissionais do campeonato. A rota /users da API publica JSONPlaceholder faz
o papel da busca por perfis.

Execucao:
    python -m locust -f locustfile_esports.py

Depois abrir http://localhost:8089 e informar usuarios, spawn rate e o host
https://jsonplaceholder.typicode.com
"""

from locust import HttpUser, between, task


class JogadorEsports(HttpUser):
    host = "https://jsonplaceholder.typicode.com"

    # tempo de "pensamento" do usuario entre uma requisicao e outra,
    # sorteado a cada iteracao entre 2 e 4 segundos
    wait_time = between(2, 4)

    @task
    def buscar_perfis_jogadores(self):
        with self.client.get(
            "/users", name="GET /users (perfis)", catch_response=True
        ) as resposta:
            if resposta.status_code != 200:
                resposta.failure(f"status inesperado: {resposta.status_code}")
                return

            try:
                perfis = resposta.json()
            except ValueError:
                resposta.failure("corpo da resposta nao e um JSON valido")
                return

            if not isinstance(perfis, list) or len(perfis) == 0:
                resposta.failure("lista de perfis vazia")

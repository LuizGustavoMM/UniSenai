# SimuladoProva - API TechShop

API RESTful em Java Spring Boot para cadastro de produtos e pedidos, usando Spring Data JPA com PostgreSQL.

## Banco de dados

O banco configurado se chama `prova_pratica`.

Para subir apenas o PostgreSQL:

```bash
docker compose up -d
```

Configuracao em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/prova_pratica
spring.datasource.username=postgres
spring.datasource.password=senai
spring.jpa.hibernate.ddl-auto=update
```

## Endpoints

### Produtos

Listar produtos:

```http
GET /produtos
```

Adicionar produto:

```http
POST /produtos
Content-Type: application/json

{
  "nome": "Notebook",
  "descricao": "Notebook para estudos",
  "preco": 3500.00,
  "quantidadeEstoque": 10
}
```

Consultar detalhes de um produto:

```http
GET /produtos/{id}
```

### Pedidos

Listar pedidos:

```http
GET /pedidos
```

Adicionar novo pedido:

```http
POST /pedidos
Content-Type: application/json

{
  "nomeCliente": "Joao",
  "status": "CRIADO",
  "produtoIds": [1, 2]
}
```

Alterar pedido:

```http
PUT /pedidos/{id}
Content-Type: application/json

{
  "nomeCliente": "Joao Silva",
  "status": "PAGO",
  "produtoIds": [1, 3]
}
```

Alterar produtos de um pedido:

```http
PUT /pedidos/{id}/produtos
Content-Type: application/json

{
  "produtoIds": [2, 3]
}
```

Remover pedido:

```http
DELETE /pedidos/{id}
```

# Sistema de Consulta de Estoque com gRPC

### Gerar arquivos a partir do .proto

### py -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. .\consultaEstoque.proto

## 1. O que a atividade pede

Voce precisa criar 3 partes:

1. Um contrato `.proto` para definir como cliente e servidor conversam.
2. Um servidor que tenha uma lista de produtos em memoria.
3. Um cliente que pergunte o codigo do produto no terminal e exiba o resultado.

## 2. Exemplo simples do `.proto`

```proto
message ConsultaProdutoRequest {
  string codigo_produto = 1;
}
```

### Explicacao

Esse trecho cria a mensagem que o cliente envia para o servidor.
Ela carrega apenas o `codigo_produto`, porque esse e o dado necessario para fazer a busca.

Outro exemplo:

```proto
message ProdutoResponse {
  string nome_produto = 1;
  double preco = 2;
  int32 quantidade_estoque = 3;
}
```

### Explicacao

Essa e a resposta do servidor.
Quando o produto existe, o servidor devolve:

- `nome_produto`
- `preco`
- `quantidade_estoque`

Exemplo do servico:

```proto
service EstoqueService {
  rpc buscarProduto (ConsultaProdutoRequest) returns (ProdutoResponse);
}
```

### Explicacao

Aqui estamos dizendo que existe um servico chamado `EstoqueService`.
Dentro dele existe o metodo remoto `buscarProduto`.

Na pratica funciona assim:

- o cliente envia uma `ConsultaProdutoRequest`
- o servidor responde com `ProdutoResponse`

## 3. Exemplo simples do servidor

```python
produtos = {
    "001": {"nome": "Mouse", "preco": 50.0, "quantidade": 10}
}
```

### Explicacao

Essa estrutura funciona como um banco de dados simples em memoria.
Quando chegar o codigo `"001"`, o servidor encontra esse item no dicionario e monta a resposta.

Exemplo da busca:

```python
produto = produtos.get(codigo)
if produto is None:
    context.set_code(grpc.StatusCode.NOT_FOUND)
    context.set_details("Produto nao encontrado.")
```

### Explicacao

Se o codigo nao existir, o servidor devolve um erro `NOT_FOUND`.
Isso atende ao requisito que pede erro ou mensagem de produto nao encontrado.

## 4. Exemplo simples do cliente

```python
codigo = input("Digite o codigo do produto: ").strip()
```

### Explicacao

O cliente pede o codigo ao usuario pelo terminal, exatamente como a atividade exige.

Depois ele chama o servidor:

```python
resposta = stub.buscarProduto(requisicao)
```

### Explicacao

Esse comando faz a chamada remota via gRPC.
Mesmo parecendo uma funcao normal, o programa esta se comunicando com outro processo pela rede.

## 5. Como eu faria

Eu faria exatamente de um jeito simples e direto:

- `.proto` com apenas os campos pedidos
- servidor com 4 produtos em um dicionario
- erro `NOT_FOUND` quando o codigo nao existir
- cliente lendo o codigo pelo terminal
- exibicao organizada dos dados retornados

Os arquivos criados foram:

- `consultaEstoque.proto`
- `servidor.py`
- `cliente.py`
- `requirements.txt`
- `gerar_proto.ps1`

## 6. Como executar

Instale as dependencias:

```powershell
pip install -r requirements.txt
```

Gere os arquivos do gRPC:

```powershell
.\gerar_proto.ps1
```

Isso deve criar:

- `consultaEstoque_pb2.py`
- `consultaEstoque_pb2_grpc.py`

Rode o servidor:

```powershell
py .\servidor.py
```

Em outro terminal, rode o cliente:

```powershell
py .\cliente.py
```

## 7. Exemplo de uso

Se o usuario digitar:

```text
001
```

Uma saida esperada seria parecida com:

```text
=== Produto encontrado ===
Codigo: 001
Nome: Notebook Dell Inspiron
Preco: R$ 3499.90
Quantidade em estoque: 7
```

Se digitar um codigo inexistente:

```text
999
```

A saida pode ser:

```text
=== Erro na consulta ===
Codigo consultado: 999
Status: NOT_FOUND
Mensagem: Produto nao encontrado.
```

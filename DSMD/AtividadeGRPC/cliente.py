import grpc

import consultaEstoque_pb2
import consultaEstoque_pb2_grpc


def run():
    codigo = input("Digite o codigo do produto: ").strip()

    with grpc.insecure_channel("localhost:50051") as channel:
        stub = consultaEstoque_pb2_grpc.EstoqueServiceStub(channel)
        requisicao = consultaEstoque_pb2.ConsultaProdutoRequest(
            codigo_produto=codigo
        )

        try:
            resposta = stub.buscarProduto(requisicao)
            print("\n=== Produto encontrado ===")
            print(f"Codigo: {codigo}")
            print(f"Nome: {resposta.nome_produto}")
            print(f"Preco: R$ {resposta.preco:.2f}")
            print(f"Quantidade em estoque: {resposta.quantidade_estoque}")
        except grpc.RpcError as erro:
            print("\n=== Erro na consulta ===")
            print(f"Codigo consultado: {codigo}")
            print(f"Status: {erro.code().name}")
            print(f"Mensagem: {erro.details()}")


if __name__ == "__main__":
    run()

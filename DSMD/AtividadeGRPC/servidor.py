from concurrent import futures

import grpc

import consultaEstoque_pb2
import consultaEstoque_pb2_grpc


class EstoqueServiceServicer(consultaEstoque_pb2_grpc.EstoqueServiceServicer):
    def __init__(self):
        self.produtos = {
            "001": {
                "nome_produto": "Notebook Dell Inspiron",
                "preco": 3499.90,
                "quantidade_estoque": 7,
            },
            "002": {
                "nome_produto": "Mouse Logitech Sem Fio",
                "preco": 129.90,
                "quantidade_estoque": 25,
            },
            "003": {
                "nome_produto": "Teclado Mecanico Redragon",
                "preco": 249.50,
                "quantidade_estoque": 12,
            },
            "004": {
                "nome_produto": "Monitor Samsung 24 Polegadas",
                "preco": 899.00,
                "quantidade_estoque": 4,
            },
        }

    def buscarProduto(self, request, context):
        codigo = request.codigo_produto.strip()
        produto = self.produtos.get(codigo)

        if produto is None:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            context.set_details("Produto nao encontrado.")
            return consultaEstoque_pb2.ProdutoResponse()

        return consultaEstoque_pb2.ProdutoResponse(
            nome_produto=produto["nome_produto"],
            preco=produto["preco"],
            quantidade_estoque=produto["quantidade_estoque"],
        )


def serve():
    servidor = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    consultaEstoque_pb2_grpc.add_EstoqueServiceServicer_to_server(
        EstoqueServiceServicer(), servidor
    )

    porta = "50051"
    servidor.add_insecure_port(f"[::]:{porta}")
    print(f"Servidor de estoque iniciado na porta {porta}.")

    servidor.start()
    servidor.wait_for_termination()


if __name__ == "__main__":
    serve()

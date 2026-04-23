import estoque_pb2
import estoque_pb2_grpc
import grpc
from itemEstoque import ItemEstoque

class EstoqueService(estoque_pb2_grpc.estoqueServiceServicer):

    def __init__(self):
        self.produtos = [
            ItemEstoque(1, "Monitor", 1000, 5),
            ItemEstoque(2, "Teclado", 400, 10),
            ItemEstoque(3, "Mouse", 350, 12),
            ItemEstoque(4, "Fone de Ouvido", 405, 20)
        ]

    def recuperarItemEstoque(self, request, context):
        produto = self._buscar_item(request.codigoItem)
        if produto:
            return estoque_pb2.ItemEstoqueResponse(
                codigoItem=produto[0].codigo,
                nome=produto[0].nome,
                valor=produto[0].preco,
                quantidade=produto[0].quantidade
            )
        else:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            context.set_details(f"O produto de código {request.codigoItem} não foi encontrado.")
            return estoque_pb2.ItemEstoqueResponse()

    
    def atualizarItemEstoque(self, request, context):
        produto = self._buscar_item(request.codigoItem)
        if produto:
            produto[0].quantidade = request.quantidade
            produto[0].preco = request.valor
            return estoque_pb2.ItemEstoqueResponse(
                codigoItem=produto[0].codigo,
                nome=produto[0].nome,
                valor=produto[0].preco,
                quantidade=produto[0].quantidade
            )
        else:
            context.set_code(grpc.StatusCode.NOT_FOUND)
            context.set_details(f"O produto de código {request.codigoItem} não foi encontrado.")
            return estoque_pb2.ItemEstoqueResponse()
    
    def _buscar_item(self, codigoItem):
        produto = [p for p in self.produtos if p.codigo == codigoItem]
        return produto
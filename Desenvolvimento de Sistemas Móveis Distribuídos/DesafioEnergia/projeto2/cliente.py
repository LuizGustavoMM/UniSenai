import grpc
import estoque_pb2
import estoque_pb2_grpc

def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = estoque_pb2_grpc.estoqueServiceStub(channel)
        
        opcao = 0
        while (opcao != 3):
            opcao = int(input('Escolha uma opção:\n1 - Consultar\n2 - Atualizar\n3 - Sair\n'))
            if opcao == 1:
                codigo = int(input('Informe o código: '))
                response = stub.recuperarItemEstoque(estoque_pb2.ItemEstoqueRequest(codigoItem=codigo))
                print(f"O retorno foi: \n{response}")
            elif opcao == 2:
                codigo = int(input('Informe o código: '))
                valor = float(input('Informe o valor: R$'))
                quantidade = int(input('Informe a quantidade: '))
                response = stub.atualizarItemEstoque(estoque_pb2.UpdateItemEstoqueRequest(
                    codigoItem=codigo,
                    valor=valor,
                    quantidade=quantidade                    
                ))
                print(f"{response}")


if __name__ == '__main__':
    run()

    
import grpc
from concurrent import futures
import estoque_pb2
import estoque_pb2_grpc
from estoque_service import EstoqueService

def iniciarServidor():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))

    estoque_pb2_grpc.add_estoqueServiceServicer_to_server(EstoqueService(), server)

    port = '50051'
    server.add_insecure_port(f'[::]:{port}')

    print(f'Iniciando servidor na porta {port}')
    server.start()

    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        server.stop()

if __name__ == '__main__':
    iniciarServidor()
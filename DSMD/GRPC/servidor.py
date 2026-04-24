import grpc
from concurrent import futures

import hello_pb2
import hello_pb2_grpc

class MeuServidor(hello_pb2_grpc.digaHelloServiceServicer):

    def digaHello(self, request, context):
        print(f"Hello, {request.nome}")
        return hello_pb2.HelloResponse(resposta=f"Olá {request.nome}, você se comunicou com o servidor!")

def iniciarServidor():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))

    hello_pb2_grpc.add_digaHelloServiceServicer_to_server(MeuServidor(), server)

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
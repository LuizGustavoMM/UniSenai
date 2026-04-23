import grpc
import hello_pb2
import hello_pb2_grpc

def run():
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = hello_pb2_grpc.digaHelloServiceStub(channel)
        request = hello_pb2.HelloRequest(nome="Zeca")
        response = stub.digaHello(request)
    print(f"Resposta recebida: {response.resposta}")

if __name__ == '__main__':
    run()

    
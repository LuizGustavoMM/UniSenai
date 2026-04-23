import random
import grpc
import consumo_pb2_grpc
import consumo_pb2
import time

def realizarLeituras():
    id_casa = f"CASA_{random.randint(100,300)}"
    consumo = 0.0
    with grpc.insecure_channel('localhost:50051') as channel:
        stub = consumo_pb2_grpc.MedidorServiceStub(channel)
        while True:
            consumo += random.uniform(5,50)
            req = consumo_pb2.LeituraRequest(
                id_casa=id_casa,
                consumo_kwh=consumo
            )
            stub.EnviarLeitura(req)
            print(f"[{id_casa}] Enviado: {consumo} kwh")
            time.sleep(20)

if __name__=="__main__":
    realizarLeituras()


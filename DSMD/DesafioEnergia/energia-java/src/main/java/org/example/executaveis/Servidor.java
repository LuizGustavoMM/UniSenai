package org.example.executaveis;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.grpc.ServidorGrpcImpl;
import org.example.rmi.ServidorMiddleware;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Servidor {

    public static void main(String[] args) {
        try {
            //Iniciando Servidor GRPC
            Server grpcServer = ServerBuilder.forPort(50051)
                    .addService(new ServidorGrpcImpl())
                    .build()
                    .start();
            System.out.println("GRPC Inicializado..");

            //Iniciando o Servidor RMI
            LocateRegistry.createRegistry(1099);
            Naming.rebind("ConsumoService", new ServidorMiddleware());
            System.out.println("RMI Inicializado...");

            System.out.println("Servidor Middleware Inicializado.");
            grpcServer.awaitTermination();
        } catch (Exception e) {
            System.out.println("Erro ao subir os servidores");
        }
    }

}

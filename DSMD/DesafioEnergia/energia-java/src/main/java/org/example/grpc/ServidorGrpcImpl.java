package org.example.grpc;

import br.com.energia.grpc.LeituraRequest;
import br.com.energia.grpc.LeituraResponse;
import br.com.energia.grpc.MedidorServiceGrpc;
import org.example.baseDados.BaseDados;

import java.io.PrintWriter;
import java.net.Socket;

public class ServidorGrpcImpl extends
        MedidorServiceGrpc.MedidorServiceImplBase {

    @Override
    public void enviarLeitura(LeituraRequest request, io.grpc.stub.StreamObserver<LeituraResponse> responseObserver) {
        System.out.println("Recebendo leitura: [" + request.getIdCasa() + "]");
        BaseDados.baseDados.put(request.getIdCasa(), request.getConsumoKwh());

        if (request.getConsumoKwh() > 500) {
            System.out.println("Disparando alerta...");
            dispararAlertaSocket(request.getIdCasa(), request.getConsumoKwh());
        }

        responseObserver.onNext(LeituraResponse.newBuilder().setSucesso(true).build());
        responseObserver.onCompleted();
    }

    private void dispararAlertaSocket(String id, double valor) {
        try {
            Socket s = new Socket("localhost", 6000);
            PrintWriter output = new PrintWriter(s.getOutputStream(), true);
            output.println("EMERGÊNCIA: Casa [" + id + "] com consumo maior que 500Kwh. Consumo atual: " + valor );
            output.close();
            s.close();
        } catch (Exception e) {
            System.out.println("Erro ao abrir conexão com o socket.");
        }
    }
}

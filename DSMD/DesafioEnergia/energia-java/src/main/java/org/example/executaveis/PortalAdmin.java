package org.example.executaveis;

import org.example.rmi.IConsumoService;

import java.rmi.Naming;
import java.util.Scanner;

public class PortalAdmin {

    public static void main(String[] args) {
        try {
            Scanner leitor = new Scanner(System.in);
            IConsumoService service = (IConsumoService) Naming.lookup("rmi://localhost/ConsumoService");
            System.out.println("Conectado ao servidor RMI");

            int opcao = -1;
            while (opcao != 9) {
                System.out.println("1 - Ver total de consumo\n" +
                        "2 - Ver consumo por residência\n" +
                        "9 - Sair");
                opcao = leitor.nextInt();
                if (opcao == 1) {
                    double consumoTotal = service.consultarConsumoTotal();
                    System.out.println("Consumo Total: " + consumoTotal + "Kwh");
                } else if (opcao == 2) {
                    System.out.println("Informe o identificador da residencia:");
                    int id = leitor.nextInt();
                    String idCompleto = "CASA_" + id;
                    double consumoResidencia = service.consultarConsumoCasa(idCompleto);
                    System.out.println("Consumo [" + id + "] - " + consumoResidencia + "Kwh");
                } else if (opcao == 9) {
                    System.out.println("saindo...");
                } else {
                    System.out.println("Opção inválida.");
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao realizar a conexão com o servidor RMI");
        }
    }

}

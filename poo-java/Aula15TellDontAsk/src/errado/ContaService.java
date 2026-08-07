package errado;

public class ContaService {
    public void transferir(ContaBancaria contaOrigem, ContaBancaria contaDestino, double valor) {
        if(contaOrigem.getSaldo() >= valor) {
            contaOrigem.depositar(-valor);
            contaDestino.depositar(valor);
            System.out.println(
                    "Transferencia realizada com sucesso"
            );
        } else {
            System.out.println("Saldo insuficiente!");
        }
    }
}

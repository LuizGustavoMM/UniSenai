package errado;

public class Main {
    public static void main(String[] args) {
        ContaBancaria conta1 = new ContaBancaria(
                1000
        );
        ContaBancaria conta2 = new ContaBancaria(
                500
        );

        ContaService service = new ContaService();
        service.transferir(
                conta1, conta2, 200
        );
    }
}
package correto;

public class Main {
    public static void main(String[] args) {
        Endereco endereco = new Endereco("Rua A", "Cidade B");
        Cliente cliente = new Cliente("Joao", endereco);
        imprimirEndereco(cliente);
    }


    public static void imprimirEndereco(Cliente cliente) {
        System.out.println(cliente.obterEnderecoCliente());
    }
}
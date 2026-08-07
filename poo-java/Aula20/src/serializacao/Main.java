package serializacao;

public class Main {
    public static void main(String[] args) {
        Cliente cliente = new Cliente("Pedro", "48996382521");
        var nomeArquivo = "cliente.ser";

        System.out.println("Gravando um arquivo de cliente...");
        var escritor = new EscritorCliente();
        escritor.escrever(cliente, nomeArquivo);
        System.out.println("Arquivo gravado.");

        System.out.println("Lendo um arquivo de cliente...");
        var leitor = new LeitorCliente();
        leitor.ler(nomeArquivo);
        System.out.println("Arquivo lido.");
    }
}

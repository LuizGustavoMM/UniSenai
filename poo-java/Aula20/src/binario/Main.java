package binario;

public class Main {
    public static void main(String[] args) {
        var nomeArquivo = "arquivo_binario.bin";

        System.out.println("Gravando um arquivo binario...");
        ///var escritor = new EscritorBinario();
        ///escritor.escrever(nomeArquivo);
        System.out.println("Arquivo gravado.");

        System.out.println("Lendo um arquivo binario...");
        var leitor = new LeitorBinario();
        leitor.ler(nomeArquivo);
        System.out.println("Arquivo lido.");
    }
}
package texto;

public class Main {
    public static void main(String[] args) {
        var nomeArquivo = "arquivo_texto.txt";

        System.out.println("Gravando um arquivo texto...");
        var escritor = new EscritorTexto();
        escritor.escrever(nomeArquivo);
        System.out.println("Arquivo gravado.");

        System.out.println("Lendo um arquivo de texto...");
        var leitor = new LeitorTexto();
        leitor.ler(nomeArquivo);
        System.out.println("Arquivo lido.");
    }
}

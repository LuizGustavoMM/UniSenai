package pdf;

public class Main {
    public static void main(String[] args) {
        System.out.println("Gravando um arquivo PDF");
        var escritor = new EscritorPDF();
        escritor.escrever("arquivo.pdf");
        System.out.println("Arquivo gravado");
    }
}

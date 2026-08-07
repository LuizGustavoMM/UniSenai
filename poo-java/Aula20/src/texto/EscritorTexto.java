package texto;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EscritorTexto {
    public void escrever(String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("olá mundo!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
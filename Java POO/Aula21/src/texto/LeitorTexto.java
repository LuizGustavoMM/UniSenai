package texto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorTexto {
    public void ler(String nomeArquivo) {
        try(BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

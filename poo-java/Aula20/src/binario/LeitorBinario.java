package binario;

import java.io.FileInputStream;
import java.io.IOException;

public class LeitorBinario {
    public void ler(String nomeArquivo) {
        try (FileInputStream fis = new FileInputStream(nomeArquivo)) {
            int dado;
            while ((dado = fis.read()) != -1) {
                System.out.println(dado);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

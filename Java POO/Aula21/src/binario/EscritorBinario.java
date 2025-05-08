package binario;

import java.io.FileOutputStream;
import java.io.IOException;

public class EscritorBinario {
    public void escrever(String nomeArquivo) {
        try (FileOutputStream fos = new FileOutputStream(nomeArquivo)) {
            fos.write(new byte[] {10, 20, 30});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

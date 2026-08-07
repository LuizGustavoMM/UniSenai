package serializacao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class EscritorCliente {
    public void escrever(Cliente cliente, String nomeArquivo) {
        try (var oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(cliente);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

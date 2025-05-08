package serializacao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LeitorCliente {
    public void ler(String nomeArquivo) {
        try (var ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            Cliente cliente = (Cliente) ois.readObject();
            System.out.println(cliente);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package cliente;

import java.util.List;

public interface IClienteService {
//    void adicionarCliente(String nome, String telefone, TipoCliente tipoCliente, String numeroDoc);
    int adicionarCliente(Cliente cliente);
    void removerCliente(int id);
    void editarCliente(int id, String novoNome, String novoTelefone);
    Cliente mostrarCliente(int id);
    List<Cliente> listarTodosClientes();
}

package fiado.clientes;

public interface IClienteService {
    // CRUD - Create Read Update Delete + List
    void cadastrarCliente(Cliente cliente);
    Cliente consultarCliente(int id);
    Cliente[] listarCliente();
}

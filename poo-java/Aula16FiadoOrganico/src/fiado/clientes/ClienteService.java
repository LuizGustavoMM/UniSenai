package fiado.clientes;

import java.util.HashMap;
import java.util.Map;

public class ClienteService implements IClienteService {

    private final Map<Integer, Cliente> clientes;

    public ClienteService() {
        this.clientes = new HashMap<>();
    }

    @Override
    public void cadastrarCliente(Cliente cliente) {
        if (this.clientes.containsKey(cliente.getId())) {
          throw new ClienteDuplicadoException("ID do cliente ja existe no cadastro.");
        }

        this.clientes.put(cliente.getId(), cliente);
    }

    @Override
    public Cliente consultarCliente(int id) {
        return null;
    }

    @Override
    public Cliente[] listarCliente() {
        Cliente [] clientes = new Cliente[this.clientes.size()];
        return this.clientes.values().toArray(clientes);
    }
}

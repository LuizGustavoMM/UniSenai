package fiado.registros;

import fiado.clientes.Cliente;

public interface IFiadoService {
    void registrarFiado(Cliente cliente, Fiado fiado);
    Fiado[] consultarRegistro(Cliente cliente);
    double consultarSaldo(Cliente cliente);
    double consultarLimiteDisponivel(Cliente cliente);
}

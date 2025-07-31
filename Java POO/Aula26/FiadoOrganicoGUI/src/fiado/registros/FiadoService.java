package fiado.registros;

import fiado.clientes.Cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiadoService implements IFiadoService {
    private final Map<Cliente, List<Fiado>> fiados;

    public FiadoService() {
        this.fiados = new HashMap<>();
    }

    @Override
    public void registrarFiado(Cliente cliente, Fiado fiado) {
        if (!this.fiados.containsKey(cliente)) {
            List<Fiado> fiados = new ArrayList<>();
            this.fiados.put(cliente, fiados);
        }
        this.fiados.get(cliente).add(fiado);
    }

    @Override
    public Fiado[] consultarRegistro(Cliente cliente) {
        Fiado[] registros;
        if (this.fiados.containsKey(cliente)) {
            List<Fiado> fiados = this.fiados.get(cliente);
            registros = new Fiado[fiados.size()];
            fiados.toArray(registros);
        } else {
            registros = new Fiado[0];
        }
        return registros;
    }

    @Override
    public double consultarSaldo(Cliente cliente) {
        double saldo = 0.0;
        Fiado[] fiados = this.consultarRegistro(cliente);
        for (Fiado fiado : fiados) {
            saldo += fiado.isPagamento() ? fiado.getValor() : fiado.getValor() * -1;
        }
        return saldo;
    }

    @Override
    public double consultarLimiteDisponivel(Cliente cliente) {
        double saldo = this.consultarSaldo(cliente);
        return cliente.getLimite() + saldo;
    }
}

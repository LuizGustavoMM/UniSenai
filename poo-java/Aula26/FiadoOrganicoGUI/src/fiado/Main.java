package fiado;

import fiado.clientes.Cliente;
import fiado.clientes.ClienteService;
import fiado.clientes.IClienteService;
import fiado.registros.Fiado;
import fiado.registros.FiadoService;
import fiado.registros.IFiadoService;
import fiado.seguranca.ISegurancaService;
import fiado.seguranca.Perfil;
import fiado.seguranca.SegurancaService;
import fiado.seguranca.Usuario;

public class Main {
    public static void main(String[] args) {
        ISegurancaService segurancaService = new SegurancaService();
        IClienteService clienteService = new ClienteService();
        IFiadoService fiadoService = new FiadoService();

        popularUsuarios(segurancaService);
        testarAutenticacao(segurancaService);

        popularClientes(clienteService);
        popularFiados(fiadoService, clienteService);
        listarFiados(fiadoService, clienteService);
    }

    public static void popularUsuarios(ISegurancaService segurancaService) {
        Usuario gerente = new Usuario(1, Perfil.GERENTE, "123", "Gerente");
        Usuario operador = new Usuario(2, Perfil.OPERADOR, "321", "Operador");
        segurancaService.adicionarUsuario(gerente);
        segurancaService.adicionarUsuario(operador);
    }

    public static void testarAutenticacao(ISegurancaService segurancaService) {
        if (segurancaService.autenticar(1, "123") != null) {
            System.out.println("Gerente autenticado");
        }
        if (segurancaService.autenticar(2, "999") == null) {
            System.out.println("Operador nao autenticado!");
        }
    }

    public static void popularClientes(IClienteService clienteService) {
        Cliente cliente1 = new Cliente(1, "Pedro", "489999988", 100);
        Cliente cliente2 = new Cliente(2, "Julia", "489997799", 250);
        Cliente cliente3 = new Cliente(3, "Maria", "489991122", 500);

        clienteService.cadastrarCliente(cliente1);
        clienteService.cadastrarCliente(cliente2);
        clienteService.cadastrarCliente(cliente3);
    }

    public static void popularFiados(IFiadoService fiadoService, IClienteService clienteService) {
        Cliente cliente = clienteService.consultarCliente(5);
        if (cliente == null) {
            System.out.println("Cliente 5 nao existe.");
        }

        cliente = clienteService.consultarCliente(2);
        if (cliente != null) {
            System.out.println("Cliente " + cliente.getNome() + " encontrado.");
        }

        Fiado fiado = new Fiado("Compra do dia", 10);
        fiadoService.registrarFiado(cliente, fiado);

        fiado = new Fiado("Compra do dia", 50);
        fiadoService.registrarFiado(cliente, fiado);

        fiado = new Fiado("Compra do dia", 75);
        fiadoService.registrarFiado(cliente, fiado);
    }

    public static void listarFiados(IFiadoService fiadoService, IClienteService clienteService) {
        System.out.println("--- Listar Fiados ---");
        for (Cliente cliente : clienteService.listarCliente()) {
            System.out.println();
            System.out.println(cliente);
            for (Fiado fiado : fiadoService.consultarRegistro(cliente)) {
                System.out.println(fiado);
            }
            double saldo = fiadoService.consultarSaldo(cliente);
            double limite = fiadoService.consultarLimiteDisponivel(cliente);
            System.out.println("Saldo Total: " + saldo);
            System.out.println("Limite Disponivel: " + limite);
        }
    }
}
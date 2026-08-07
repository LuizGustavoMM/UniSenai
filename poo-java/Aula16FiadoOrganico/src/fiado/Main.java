package fiado;

import fiado.clientes.ClienteService;
import fiado.clientes.IClienteService;
import fiado.registros.FiadoService;
import fiado.registros.IFiadoService;
import fiado.seguranca.ISegurancaService;
import fiado.seguranca.Perfil;
import fiado.seguranca.SegurancaService;
import fiado.seguranca.Usuario;

import java.util.Dictionary;

public class Main {
    public static void main(String[] args) {
        ISegurancaService segurancaService = new SegurancaService();
        IClienteService clienteService = new ClienteService();
        IFiadoService fiadoService = new FiadoService();

        popularUsuarios(segurancaService);
        testarAutenticacao(segurancaService);
    }

    public static void popularUsuarios(ISegurancaService segurancaService) {
        Usuario gerente = new Usuario(1, "Gerente", "123", Perfil.GERENTE);
        Usuario operador = new Usuario(2, "Operador", "321", Perfil.OPERADOR);
        segurancaService.adicionarUsuario(gerente);
        segurancaService.adicionarUsuario(operador);
    }

    public static void testarAutenticacao(ISegurancaService segurancaService) {
        if (segurancaService.autenticar(1, "123") != null) {
            System.out.println("Gerente autenticado");
        }
        if (segurancaService.autenticar(2, "999") ==null) {
            System.out.println("Operador nao autenticado!");
        }
    }
}
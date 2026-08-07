// Main.java
package MinimarketMVP;

import MinimarketMVP.seguranca.*;
import MinimarketMVP.clientes.*;

public class Main {
    public static void main(String[] args) {
        // Simulação de criação de gerente
        Usuario gerente = new Usuario(1, "Joana", "admin123", Perfil.GERENTE);

        // Simulação de criação de clientes
        Venda venda1 = new Venda("coca", "banana", "");
        Cliente cliente1 = new Cliente("Carlos Silva", 123456789, true);
        Cliente cliente2 = new Cliente("Marina Souza", 987654321, false);

        System.out.println("Gerente: " + gerente.getNome());
        System.out.println("Cliente Fidelizado: " + cliente1.getNome());
        System.out.println("Cliente Não Fidelizado: " + cliente2.getNome());
    }
}

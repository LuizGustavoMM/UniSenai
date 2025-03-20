package folha;

import folha.empregado.Empregado;
import folha.empregado.Gerente;
import folha.empregado.Vendedor;

import java.util.ArrayList;

public class FolhaPagamento {

    public void calcularFolha() {
        ArrayList<Empregado> empregados = this.consultarDados();
        for (Empregado empregado: empregados) {
            double salario = empregado.getSalario();
            System.out.println("Nome: " + empregado.getNome() +
                    " - Salario: " + salario);
        }
    }

    protected ArrayList<Empregado> consultarDados() {
        ArrayList<Empregado> empregados = new ArrayList<>();

        Empregado e1 = new Empregado("Pedro", 10000);
        Empregado e2 = new Empregado("Maria", 12000);

        Gerente g1 = new Gerente("Carlos", 5000, 500, 550, 10000);
        Gerente g2 = new Gerente("Fabiana", 3000, 100, 150, 30000);

        Vendedor v1 = new Vendedor("Joaquim", 1500, 1, 20000);
        Vendedor v2 = new Vendedor("Sara", 3000, 1, 5000);

        empregados.add(e1);
        empregados.add(e2);
        empregados.add(g1);
        empregados.add(g2);
        empregados.add(v1);
        empregados.add(v2);

        return empregados;
    }

}

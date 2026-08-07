package folha.empregado;

public class Gerente extends Empregado {

    private double metaMes;
    private double vendas;
    private double bonus;

    public Gerente(String nome, double salarioBase, double metaMes, double vendas, double bonus) {
        super(nome, salarioBase);
        this.metaMes = metaMes;
        this.vendas = vendas;
        this.bonus = bonus;
    }

    @Override
    public double getSalario() {
        double bonus = 0.0;
        if (this.vendas >= this.metaMes) {
            bonus = this.bonus;
        }
        return this.getSalarioBase() + bonus;
    }
}

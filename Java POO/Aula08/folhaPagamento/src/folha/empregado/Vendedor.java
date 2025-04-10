package folha.empregado;

public class Vendedor extends Empregado {

    private double percentualComissao;
    private double vendas;

    public Vendedor(String nome, double salarioBase, double percentualComissao, double vendas) {
        super(nome, salarioBase);
        this.percentualComissao = percentualComissao;
        this.vendas = vendas;

    }

    @Override
    public double getSalario() {
        return this.getSalarioBase() + (this.percentualComissao * this.vendas);
    }
}

package folha.empregado;

public class Empregado {

    private String nome;
    private double salarioBase;

    public Empregado(String nome, double salarioBase) {
        this.nome = nome;
        this.salarioBase = salarioBase;
    }
    public String getNome(){
        return this.nome;
    }

    protected double getSalarioBase() {
        return this.salarioBase;
    }
    public double getSalario() {
        return this.salarioBase;
    }

}

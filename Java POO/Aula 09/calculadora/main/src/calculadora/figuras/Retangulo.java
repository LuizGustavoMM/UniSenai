package calculadora.figuras;

public class Retangulo implements FiguraGeometrica {

    private final double largura;
    private final double altura;

    public Retangulo(double largura, double altura) {
            this.largura = largura;
            this.altura = altura;
    }
    @Override
    public double calcularArea() {
        return this.largura * this.altura;}

    @Override
    public double calcularPerimetro() {
        return 2* (this.largura + this.altura);}

    @Override
    public void finalize() {
        System.out.println("Morri!");
    }
}

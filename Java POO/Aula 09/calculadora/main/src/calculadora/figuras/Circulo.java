package calculadora.figuras;

public class Circulo implements  FiguraGeometrica {

    private final double raio;

    public Circulo(double raio) {
            this.raio = raio;

    }
    @Override
    public double calcularArea() {
        return Math.PI * (this.raio * this.raio);
    }

    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * raio;
    }
}

package correto;

public class Luz {
    private ILampada lampada;

    public Luz(ILampada lampada) {
        this.lampada = lampada;
    }
    public void ligar() {
        this.lampada.acender();
    }
    public void desligar() {
        this.lampada.apagar();
    }
}

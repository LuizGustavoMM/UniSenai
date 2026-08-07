package errado;

public class Luz {
    private Lampada lampada ;
    public Luz() {
        this.lampada = new Lampada();
    }
    public void ligar() {
        this.lampada.acender();
    }
    public void desligar() {
        this.lampada.apagar();
    }
}

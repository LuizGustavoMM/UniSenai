package correto;

public class Main {
    public static void main(String[] args) {
        ILampada lampada = new Lampada();
        Luz luz = new Luz(lampada);
        luz.ligar();
        luz.desligar();
    }
}

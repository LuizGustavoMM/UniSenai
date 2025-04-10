package correto;

public class SelvagemPato implements Grasnador, Voador, Nadador {
    @Override
    public void grasnar() {
        System.out.println("Quack!");
    }
    @Override
    public void nadar() {
        System.out.println("Splash. splash...!");
    }
    public void voar() {
        System.out.println("3, 2, 1... decolando!");
    }
}

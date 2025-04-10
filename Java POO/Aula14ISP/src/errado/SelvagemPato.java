package errado;

public class SelvagemPato implements Pato {
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

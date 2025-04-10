package errado;

public class BorrachaPato implements Pato {
    @Override
    public void grasnar() {
        System.out.println("Quack!");
    }
    @Override
    public void nadar() {
        throw new RuntimeException("Só sei boiar :(");
    }
    public void voar() {
        throw new RuntimeException("Não sei voar");
    }
}

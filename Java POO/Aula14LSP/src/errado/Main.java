package errado;

public class Main {
    public static void main(String[] args) {
        Pato patoDomestico = new DomesticoPato();
        patoDomestico.grasnar();

        Pato patoSelvagem = new SelvagemPato();
        patoSelvagem.grasnar();

        Pato patoBorracha = new BorrachaPato();
        patoBorracha.grasnar();
    }
}
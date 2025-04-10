package correto;

public class Main {
    public static void main(String[] args) {
        Grasnador patoDomestico = new DomesticoPato();
        patoDomestico.grasnar();

        Voador patoDomesticoVoador = (Voador) patoDomestico;
        patoDomesticoVoador.voar();

        ((Nadador) patoDomestico). nadar();

        Grasnador patoSelvagem = new SelvagemPato();
        patoSelvagem.grasnar();

        Voador patoSelvagemVoador = (Voador) patoSelvagem;
        patoSelvagemVoador.voar();

        ((Nadador) patoSelvagem). nadar();

        Grasnador patoBorracha = new BorrachaPato();
        patoBorracha.grasnar();
    }
}
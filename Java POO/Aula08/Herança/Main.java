public class Main {

    public static void main(String[] args) {
        Animal meuAnimal = new Animal();
        meuAnimal.emitirSom();

        Cachorro meuCachorro = new Cachorro();
        meuCachorro.emitirSom();

        Gato meuGato = new Gato();
        meuGato.emitirSom();
        
        //----------------

        Animal outroAnimal = meuCachorro;
        outroAnimal.emitirSom();

        outroAnimal = meuGato;
        outroAnimal.emitirSom();

        //-----------------

        System.out.println(meuAnimal);
        System.out.println(meuCachorro);
        System.out.println(outroAnimal);

    }
}

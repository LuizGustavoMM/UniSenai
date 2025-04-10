public class Cachorro extends Animal {
    @Override
    public void emitirSom() {
        //super.emitirSom();
        System.out.println("Au au au");
    }
    
    public void abanarRabo() {
        System.out.println("Abanando rabo");
    }

    @Override
    public String toString() {
        return "Sou um cachorro: " + super.hashCode();
    }

}

public class DesviosLacos {
    public static void main(String[] args) {
        
        int x = 10;
        if (x == 5) {
                System.out.println("Vale 5");
        } else if (x > 10){
        //} else if (!(x <= 10)){ negação
                System.out.println("Vale mais que 10");
        } else {
                System.out.println("Vale " + x);
        }

        int ternario = x > 100 ? 1000 : 0;
        
        String s = "teste";
        if (s.equals("teste")) {
                System.out.println("Eh um teste");
        } else {
                System.out.println("Nao eh um teste");
        }

        int custo = 100;
        int lucro = 10;
        boolean importado = false;

        if (custo > 10 || lucro < 10 && !importado) {
                System.out.println("Comprar!");
        }

        //------------Lacos

        int i = 0;
        while (i < 10) {
                System.out.println("Laco " + i);
                i++;
        }
        
        i = 0;
        do {
                System.out.println("Laco " + i);
                i++;
        } while (i < 10);

        i = 0;
        while (i++ < 10) {
                if (i < 5) {
                        continue;
                }
                System.out.println("Laco " + i);
                if (i > 7) {
                    break;
                }
        }

    }
}
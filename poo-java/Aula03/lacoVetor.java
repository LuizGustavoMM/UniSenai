public class lacoVetor {
        public static void main(String[] args) {

                int acumulador = 0;
                int[] valores = new int[] {2, 3, 5, 7, 11};

                // int valores[] = new int[] {2, 3, 5, 7, 11};
                
                // int[] valores = new int[5];
                // valores[0] = 2;
                // valores[1] = 3;
                // ...

                for(int i = 0; i < valores.length; i++) {
                        acumulador += valores[i];
                        System.out.println("Contador: " + i);
                        System.out.println("Acumulador: " + acumulador);
                        
                }

                // int = 0;
                //while (i < valores.lenght) {
                //      i++;
                // }

                acumulador = 0;
                for(int v : valores) {
                        acumulador += v;
                        System.out.println("Acumulador: " + acumulador);
                }
        }
}

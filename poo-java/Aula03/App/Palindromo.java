public class Palindromo {
        public static void main(String[] args) {
            String palavra = "OROBORA";
            System.out.println("A palavra escolhido eh: " + palavra);

            char[] letras = palavra.toCharArray();
            int tamanho = letras.length;

            int i = 0;
            for (; i < tamanho; i++) {
                    if (letras[i] == letras[tamanho -1 - i]) {
                        continue;
                    } else {
                        System.out.println("nao eh");
                        break;
                    }
            }
            if (i == tamanho) {
                    System.out.println("eh palindromo");
            }
                    //System.out.println(letras[i]);
            
        }
}

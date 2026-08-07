import java.util.Scanner;

public class ImparPar {
    public static void main(String[] args) {
    
        Scanner scanner = new Scanner(System.in);

        int impar = 0;
        int par = 0;
        int i = 0;
        while (i < 10) {
            System.out.print("Digite 10 numeros: ");
            int num = scanner.nextInt();
            if (num % 2 == 1) {
                impar++;
            }
            else {
                par++;
            }
            i++;
        }
        System.out.println(impar +(" numeros impares"));
        System.out.println(par +(" numeros pares"));
    
        scanner.close();
    }
}
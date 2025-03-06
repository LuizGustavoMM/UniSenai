import java.util.Scanner;
public class SwitchMetodo {
        // void
        // assinatura do metodo (nao pode ser igual)
        public static double calcular(double num1, double num2, char operador) {
            double resultado = Double.NaN;    
            switch(operador) {
                        case '+': 
                                resultado = soma(num1, num2);
                                break;
                        case '-':
                                resultado = subtracao(num1, num2);
                                break;
                        case '/':
                                resultado = divisao(num1, num2);
                                break;
                        case '*':
                                resultado = multiplicacao(num1, num2);
                                break;
                        default:
                             System.out.println("ERRO: Operador invalido");
                }       
                return resultado;
        }

        public static double soma(double a, double b) {
                return a + b;
        }

        public static double subtracao(double a, double b) {
                return a - b;
        }

        public static double divisao(double a, double b) {
                return a / b ;
        }

        public static double multiplicacao(double a, double b) {
                return a * b;
        }
        public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Digite o primeiro numero: ");
                double num1 = scanner.nextDouble();
                System.out.print("Digite o segundo numero: ");
                double num2 = scanner.nextDouble();
                System.out.print("Digite a operacao (+, -, /, *): ");
                char operador = scanner.next().charAt(0);
                double resultado = calcular(num1, num2, operador); //apesar de ter o mesmo nome "resultado" faz parte  de um escopo diferente

                if (!Double.isNaN(resultado)) {
                        System.out.printf("Resultado: %.2f%n", resultado);
                }
                scanner.close();
        }   
}

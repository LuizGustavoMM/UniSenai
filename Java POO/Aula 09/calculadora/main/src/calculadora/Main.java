package calculadora;

import calculadora.figuras.FiguraGeometrica;
import calculadora.figuras.Retangulo;
import calculadora.figuras.Circulo;
import calculadora.figuras.Quadrado;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Scanner;

public class Main {
    private static Scanner scanner;
    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.println("===Calculadora de Figuras Geometricas===");

        boolean continuar = true;
        while (continuar) {
            continuar = menu();
        }

        scanner.close();
    }

    public static boolean menu() {
        System.out.println("Escolha uma figura: ");
        System.out.println("1 - Retangulo");
        System.out.println("2 - Circulo");
        System.out.println("3 - Quadrado");
        System.out.println("0 - Sair");

        System.out.println("");
        System.out.print("Opção: ");
        int escolha = scanner.nextInt();

        FiguraGeometrica figura = null;
        boolean continuar = true;
        switch (escolha) {
            case 0 -> {
                continuar = false;
            }
            case 1 -> {
                System.out.print("Digite a largura: ");
                double largura = scanner.nextDouble();
                System.out.print("Digite a altura: ");
                double altura = scanner.nextDouble();
                figura = new Retangulo(largura, altura);
            }
            case 2 -> {
                System.out.print("Digite o raio: ");
                double raio = scanner.nextDouble();
                figura = new Circulo(raio);
            }
            case 3 -> {
                System.out.print("Digite a largura: ");
                double lado = scanner.nextDouble();
                figura = new Quadrado(lado);
            }
                default -> {
                System.out.println("Opção invalida!");
            }
        }

        if (figura != null) {
            System.out.printf("Area: %.2f%n", figura.calcularArea());
            System.out.printf("Perimetro: %.2f%n", figura.calcularPerimetro());
        }
        return continuar;
    }

}
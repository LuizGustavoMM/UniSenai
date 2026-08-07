package correto;

public class Main {
   public static void main(String[] args) {
       ContaBancaria conta1 = new ContaBancaria(1000);
       ContaBancaria conta2 = new ContaBancaria(500);

       if (conta1.transferirPara(conta2, 200)) {
           System.out.println("Transferencia realizada com sucesso!");
       } else {
           System.out.println("Saldo insuficiente!");
       }
   }
}
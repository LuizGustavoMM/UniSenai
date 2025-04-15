package correto;

public class ContaBancaria {
    private double saldo = 0.00;

    public ContaBancaria(double saldo) {
        if (saldo < 0) {
            throw new RuntimeException("Somente valores positivos sao autorizados" + " para o saldo incial");
        }
        this.saldo = saldo;
    }

    public double getSaldo() {
        return saldo;
    }
    public void depositar(double valor) {
        if(valor <= 0) {
            throw new RuntimeException("Somente valores positivos sao autorizados para " + "o deposito");
        }
        this.saldo +=valor;
    }

    public void sacar(double valor) {
        if (valor <= 0) {
            throw new RuntimeException("Somente valores positivos sao autorizados para " + "o saque");
        }
        if (valor > this.saldo) {
            throw new RuntimeException("O valor de saque solicitado eh superior ao valor" + "do saldo");
        }
        this.saldo -= valor;
    }

    public boolean transferirPara(ContaBancaria contaDestino, double valor) {
        if (this.getSaldo() < valor) {
            return false;
        }

        this.sacar(valor);
        contaDestino.depositar(valor);
        return true;
    }
}

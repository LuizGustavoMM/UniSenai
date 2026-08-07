package fiado.registros;

import java.time.LocalDateTime;

public class Fiado {
    private final LocalDateTime dataHora;
    private final String descricao;
    private final double valor;
    private final boolean isPagamento;

    public Fiado(String descricao, double valor, boolean isPagamento) {
        if (valor <= 0) {
            throw new ValorNaoPermitidoException("O valor deve ser positivo.");
        }
        this.dataHora = LocalDateTime.now();
        this.descricao = descricao;
        this.valor = valor;
        this.isPagamento = isPagamento;
    }

    public Fiado(String descricao, double valor) {
        this(descricao, valor, false);
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public boolean isPagamento() {
        return isPagamento;
    }

    @Override
    public String toString() {
        return "Fiado{" +
                "dataHora=" + dataHora +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", isPagamento=" + isPagamento +
                '}';
    }
}

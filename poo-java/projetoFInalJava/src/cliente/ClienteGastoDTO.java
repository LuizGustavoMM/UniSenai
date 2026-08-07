package cliente;

public class ClienteGastoDTO {
    private int id;
    private String nome;
    private double totalGasto;

    public ClienteGastoDTO(int id, String nome, double totalGasto) {
        this.id = id;
        this.nome = nome;
        this.totalGasto = totalGasto;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getTotalGasto() {
        return totalGasto;
    }
}

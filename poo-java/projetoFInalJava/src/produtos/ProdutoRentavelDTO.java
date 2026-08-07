package produtos;

public class ProdutoRentavelDTO {
    private int id;
    private String nome;
    private double totalVendido;

    public ProdutoRentavelDTO(int id, String nome, double totalVendido) {
        this.id = id;
        this.nome = nome;
        this.totalVendido = totalVendido;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getTotalVendido() {
        return totalVendido;
    }
}

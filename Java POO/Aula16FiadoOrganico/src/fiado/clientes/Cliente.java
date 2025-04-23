package fiado.clientes;

public class Cliente {
    private final int id;
    private String nome;
    private String telefone;
    private double limite;

    public Cliente(String nome, int id, String telefone, double limite) {
        this.nome = nome;
        this.id = id;
        this.telefone = telefone;
        this.limite = limite;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", limite=" + limite +
                '}';
    }
}

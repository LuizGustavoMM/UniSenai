package fiado.clientes;

public class Cliente {
    private final int id; // getter
    private String nome; // getter & setter
    private String telefone; // getter & setter
    private double limite; // getter & setter

    public Cliente(String nome, String telefone, double limite) {
        this(0, nome, telefone, limite);
    }

    public Cliente(int id, String nome, String telefone, double limite) {
        this.id = id;
        this.nome = nome;
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

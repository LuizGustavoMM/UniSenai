package MinimarketMVP.clientes;

public class Cliente {
    private final int cpf;
    private String nome;
    private boolean fidelidade;

    public Cliente(String nome, int cpf, boolean fidelidade) {
        this.nome = nome;
        this.cpf = cpf;

    }

    public int getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isFidelidade() {
        return fidelidade;
    }

    public void setFidelidade(boolean fidelidade) {
        this.fidelidade = fidelidade;
    }

}

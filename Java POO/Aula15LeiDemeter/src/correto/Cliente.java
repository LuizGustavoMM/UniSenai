package correto;

public class Cliente {
    private String nome;
    private Endereco endereco;

    public Cliente(String nome, Endereco endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }
    public String getNome() {
        return nome;
    }
    public String obterEnderecoCliente() {
        return "Endereco do Cliente: " + this.endereco.obterEnderecoCompleto();
    }
}

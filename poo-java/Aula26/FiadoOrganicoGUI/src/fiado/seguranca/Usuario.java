package fiado.seguranca;

public class Usuario {
    private final int id; // getter
    private String nome; // getter & setter
    private String senha; // setter
    private Perfil perfil; // getter & setter

    public Usuario(int id, Perfil perfil, String senha, String nome) {
        this.id = id;
        this.perfil = perfil;
        this.senha = senha;
        this.nome = nome;
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

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }
}

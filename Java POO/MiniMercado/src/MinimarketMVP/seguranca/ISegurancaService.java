package MinimarketMVP.seguranca;

public interface ISegurancaService {
    void adicionarUsuario(Usuario usuario);
    Usuario autenticar(int id, String senha);
}
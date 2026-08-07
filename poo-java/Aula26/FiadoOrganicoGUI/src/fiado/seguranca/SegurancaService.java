package fiado.seguranca;

import java.util.HashMap;
import java.util.Map;

public class SegurancaService implements ISegurancaService {

    private final Map<Integer, Usuario> usuarios;

    public SegurancaService() {
        this.usuarios = new HashMap<>();
    }

    @Override
    public void adicionarUsuario(Usuario usuario) {
        if (this.usuarios.containsKey(usuario.getId())) {
            throw new UsuarioDuplicadoException("ID do usuario jah existe no cadastro.");
        }
        this.usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Usuario autenticar(int id, String senha) {
        Usuario usuario = this.usuarios.get(id);
        if (usuario != null && usuario.autenticar(senha)) {
            return usuario;
        }
        return null;
    }
}

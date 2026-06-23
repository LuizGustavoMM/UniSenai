package br.senai.LojaProduto.service;

import br.senai.LojaProduto.model.AutenticacaoDTO;
import br.senai.LojaProduto.model.Usuario;
import br.senai.LojaProduto.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario save(Usuario usuario){
        String encode = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encode);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }

    public String autenticar(AutenticacaoDTO autenticacaoDTO){
        Optional<Usuario> user = usuarioRepository
                .findByUsernameAndSenha(autenticacaoDTO.getUsername(),
                        autenticacaoDTO.getSenha());
        if (user.isPresent()){
            String token = gerarToken();
            user.get().setToken(token);

            usuarioRepository.save(user.get());
            return token;
        }
        throw new IllegalArgumentException();
    }

    public boolean autorizacao(String token){
        Optional<Usuario> usuario = usuarioRepository.findByToken(token);
        if(usuario.isPresent())
            return true;
        return false;
    }

    public boolean autenticacao(String username, String senha){
        Optional<Usuario> user = usuarioRepository.findByUsernameAndSenha(username, senha);
        if (user.isPresent()){
            return true;
        }
        return false;
    }

    public String gerarToken(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}

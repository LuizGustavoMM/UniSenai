package br.senai.LojaProduto.repository;

import br.senai.LojaProduto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameAndSenha(String username, String senha);

    Optional<Usuario> findByToken(String token);

    Optional<Usuario> findByUsername(String username);

}

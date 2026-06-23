package br.senai.LojaProduto.security;

import br.senai.LojaProduto.model.Usuario;
import br.senai.LojaProduto.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceCustom(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);

        if(usuario.isEmpty()){
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        User user = new User(usuario.get().getUsername(), usuario.get().getSenha(), authorities);

        return user;
    }
}

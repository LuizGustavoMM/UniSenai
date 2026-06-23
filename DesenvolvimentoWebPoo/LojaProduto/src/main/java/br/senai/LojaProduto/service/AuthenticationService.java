package br.senai.LojaProduto.service;

import br.senai.LojaProduto.model.AutenticacaoDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String autenticar(AutenticacaoDTO auth){
        UsernamePasswordAuthenticationToken usernamePasswordAuthentication =
                new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getSenha());
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthentication);

        UserDetails usuarioAutenticado = (UserDetails) authenticate.getPrincipal();

        String tokenGerado = jwtService.gerarToken(usuarioAutenticado);

        return tokenGerado;
    }

}

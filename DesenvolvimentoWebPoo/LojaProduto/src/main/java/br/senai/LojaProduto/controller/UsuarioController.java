package br.senai.LojaProduto.controller;

import br.senai.LojaProduto.model.AutenticacaoDTO;
import br.senai.LojaProduto.model.Usuario;
import br.senai.LojaProduto.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/autenticacao")
    public ResponseEntity<?> autenticar(@RequestBody AutenticacaoDTO autenticacaoDTO){
        try{
            String token = usuarioService.autenticar(autenticacaoDTO);
            return ResponseEntity.ok("token: "+token);
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso Negado");
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> adicionar(@RequestBody Usuario usuario){
        try{
            usuarioService.save(usuario);
            return ResponseEntity.ok("Usuario Criado");
        }catch (Exception ex){
            return ResponseEntity.badRequest().body("");
        }
    }

}

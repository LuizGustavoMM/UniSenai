
package br.senai.LojaProduto.controller;

import br.senai.LojaProduto.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.senai.LojaProduto.model.Categoria;
import br.senai.LojaProduto.service.CategoriaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;
    
    public CategoriaController(br.senai.LojaProduto.service.CategoriaService categoriaService, UsuarioService usuarioService) {
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> cadastrarCategoria(@RequestBody Categoria categoria){
        try {
            categoria = categoriaService.cadastrarCategoria(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar categoria: " + e.getMessage());
        }
    }
    
    

  @GetMapping("/buscar")
  public ResponseEntity<?> listarTodos() {
    try {
      List<Categoria> categorias = categoriaService.buscarCategorias();
      return ResponseEntity.ok(categorias);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao listar categorias: " + e.getMessage());
    }
  }
    
}

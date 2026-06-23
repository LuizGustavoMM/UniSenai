package br.senai.LojaProduto.controller;

import br.senai.LojaProduto.model.DescricaoProdutoResponseDTO;
import br.senai.LojaProduto.model.NovoProdutoRequestDTO;
import br.senai.LojaProduto.model.Produto;
import br.senai.LojaProduto.service.ProdutoService;
import br.senai.LojaProduto.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

  private final ProdutoService produtoService;
  private final UsuarioService usuarioService;

  public ProdutoController(ProdutoService produtoService, UsuarioService usuarioService){
    this.produtoService = produtoService;
      this.usuarioService = usuarioService;
  }

  @PostMapping("/add")
  public ResponseEntity<?> cadastrar(@RequestBody NovoProdutoRequestDTO produtoDto,
                                     @RequestHeader(value = "Authorization") String token) {
    try {
      boolean autenticacao = usuarioService.autorizacao(token);
      if(!autenticacao)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso não permitido");
      Produto produto = produtoService.cadastrarProduto(produtoDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(produto.getCodigo());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao cadastrar produto: " + e.getMessage());
    }
  }

  @GetMapping("/buscar")
  public ResponseEntity<?> listarTodos() {
    try {
      List<Produto> produtos = produtoService.buscarProdutos();
      return ResponseEntity.ok(produtos);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao listar produtos: " + e.getMessage());
    }
  }

  @GetMapping("/buscar/{codigo}")
  public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
    try {
      Optional<Produto> produto = produtoService.buscarProdutoPorCodigo(codigo);
      return produto.<ResponseEntity<?>>map(ResponseEntity::ok)
              .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .body("Produto não encontrado"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao buscar produto: " + e.getMessage());
    }
  }

  @PutMapping("/alterar/{codigo}")
  public ResponseEntity<?> atualizar(@PathVariable Long codigo,
                                     @RequestBody Produto produtoAtualizado) {
    try {
      Optional<Produto> produto = produtoService.alterarProduto(codigo, produtoAtualizado);

      if(produto.isPresent())
        return ResponseEntity.ok(produto.get());

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body("Produto não encontrado");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao atualizar produto: " + e.getMessage());
    }
  }

  @DeleteMapping("/excluir/{codigo}")
  public ResponseEntity<?> excluir(@PathVariable Long codigo) {
    try {
      boolean removido = produtoService.excluirProduto(codigo);

      if (removido) {
        return ResponseEntity.ok("Produto removido com sucesso!");
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Produto não encontrado");
      }

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao excluir produto: " + e.getMessage());
    }
  }
  
  @GetMapping("/descricao")
  public ResponseEntity<?> buscarProdutoCategoria(){
      try {
      List<DescricaoProdutoResponseDTO> produtos = produtoService.buscarDescricao();
      return ResponseEntity.ok(produtos);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body("Erro ao listar produtos: " + e.getMessage());
    }
  }
  
}
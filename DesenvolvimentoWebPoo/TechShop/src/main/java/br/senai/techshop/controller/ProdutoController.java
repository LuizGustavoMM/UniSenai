package br.senai.techshop.controller;

import br.senai.techshop.model.Produto;
import br.senai.techshop.service.ProdutoService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Produto> produtos = produtoService.buscarProdutos();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar produtos: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Produto produto) {
        try {
            Produto salvo = produtoService.cadastrarProduto(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
        try {
            Optional<Produto> produto = produtoService.buscarProdutoPorCodigo(codigo);

            if (produto.isPresent()) {
                return ResponseEntity.ok(produto.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar produto: " + e.getMessage());
        }
    }

}

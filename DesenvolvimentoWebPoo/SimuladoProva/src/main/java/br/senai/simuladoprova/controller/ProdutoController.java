package br.senai.simuladoprova.controller;

import br.senai.simuladoprova.dto.ProdutoRequestDTO;
import br.senai.simuladoprova.model.Produto;
import br.senai.simuladoprova.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    // Aqui o controller chama a camada de service, onde fica a regra de negocio de produtos.
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        // Endpoint GET /produtos: lista todos os produtos cadastrados no banco.
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Produto> adicionar(@RequestBody ProdutoRequestDTO dto) {
        // Endpoint POST /produtos: recebe os dados do produto e cadastra um novo registro.
        Produto produto = produtoService.adicionar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        // Endpoint GET /produtos/{id}: consulta os detalhes de um produto especifico.
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }
}

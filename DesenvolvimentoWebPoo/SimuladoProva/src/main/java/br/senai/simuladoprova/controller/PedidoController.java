package br.senai.simuladoprova.controller;

import br.senai.simuladoprova.dto.PedidoProdutosRequestDTO;
import br.senai.simuladoprova.dto.PedidoRequestDTO;
import br.senai.simuladoprova.model.Pedido;
import br.senai.simuladoprova.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    // Aqui o controller recebe as requisicoes HTTP e delega a regra de pedidos para o service.
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        // Endpoint GET /pedidos: lista todos os pedidos salvos no banco.
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Pedido> adicionar(@RequestBody PedidoRequestDTO dto) {
        // Endpoint POST /pedidos: cria um novo pedido com cliente, status e produtos.
        Pedido pedido = pedidoService.adicionar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> alterar(@PathVariable Long id, @RequestBody PedidoRequestDTO dto) {
        // Endpoint PUT /pedidos/{id}: altera os dados gerais de um pedido existente.
        return ResponseEntity.ok(pedidoService.alterar(id, dto));
    }

    @PutMapping("/{id}/produtos")
    public ResponseEntity<Pedido> alterarProdutos(@PathVariable Long id,
                                                  @RequestBody PedidoProdutosRequestDTO dto) {
        // Endpoint PUT /pedidos/{id}/produtos: troca apenas a lista de produtos do pedido.
        return ResponseEntity.ok(pedidoService.alterarProdutos(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        // Endpoint DELETE /pedidos/{id}: remove um pedido especifico do banco.
        pedidoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}

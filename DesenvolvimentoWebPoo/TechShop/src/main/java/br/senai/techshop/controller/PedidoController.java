package br.senai.techshop.controller;

import br.senai.techshop.model.Pedido;
import br.senai.techshop.model.PedidoRequestDTO;
import br.senai.techshop.service.PedidoService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody PedidoRequestDTO pedidoDto) {
        try {
            Pedido pedido = pedidoService.cadastrarPedido(pedidoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar pedido: " + e.getMessage());
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> alterar(@PathVariable Long codigo, @RequestBody PedidoRequestDTO pedidoDto) {
        try {
            Optional<Pedido> pedido = pedidoService.alterarPedido(codigo, pedidoDto);

            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao alterar pedido: " + e.getMessage());
        }
    }

    @PutMapping("/{codigo}/produtos")
    public ResponseEntity<?> alterarProdutos(@PathVariable Long codigo, @RequestBody List<Long> produtos) {
        try {
            Optional<Pedido> pedido = pedidoService.alterarProdutosDoPedido(codigo, produtos);

            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao alterar produtos do pedido: " + e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> remover(@PathVariable Long codigo) {
        try {
            boolean removido = pedidoService.removerPedido(codigo);

            if (removido) {
                return ResponseEntity.ok("Pedido removido com sucesso!");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover pedido: " + e.getMessage());
        }
    }

}

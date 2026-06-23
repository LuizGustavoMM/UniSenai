package br.senai.techshop.service;

import br.senai.techshop.model.Pedido;
import br.senai.techshop.model.PedidoRequestDTO;
import br.senai.techshop.model.Produto;
import br.senai.techshop.repository.PedidoRepository;
import br.senai.techshop.repository.ProdutoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public Pedido cadastrarPedido(PedidoRequestDTO pedidoDto) {
        Pedido pedido = new Pedido();
        pedido.setCliente(pedidoDto.getCliente());
        pedido.setStatus(pedidoDto.getStatus());
        pedido.setDataPedido(LocalDate.now());
        pedido.setProdutos(produtoRepository.findAllById(pedidoDto.getProdutos()));
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> alterarPedido(Long codigo, PedidoRequestDTO pedidoDto) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(codigo);

        if (pedidoExistente.isEmpty()) {
            return Optional.empty();
        }

        Pedido pedido = pedidoExistente.get();
        pedido.setCliente(pedidoDto.getCliente());
        pedido.setStatus(pedidoDto.getStatus());
        return Optional.of(pedidoRepository.save(pedido));
    }

    public Optional<Pedido> alterarProdutosDoPedido(Long codigo, List<Long> produtosCodigos) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(codigo);

        if (pedidoExistente.isEmpty()) {
            return Optional.empty();
        }

        Pedido pedido = pedidoExistente.get();
        List<Produto> produtos = produtoRepository.findAllById(produtosCodigos);
        pedido.setProdutos(produtos);
        return Optional.of(pedidoRepository.save(pedido));
    }

    public boolean removerPedido(Long codigo) {
        if (!pedidoRepository.existsById(codigo)) {
            return false;
        }
        pedidoRepository.deleteById(codigo);
        return true;
    }

}

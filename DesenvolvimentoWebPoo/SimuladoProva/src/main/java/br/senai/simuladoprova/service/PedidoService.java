package br.senai.simuladoprova.service;

import br.senai.simuladoprova.dto.PedidoProdutosRequestDTO;
import br.senai.simuladoprova.dto.PedidoRequestDTO;
import br.senai.simuladoprova.model.Pedido;
import br.senai.simuladoprova.model.Produto;
import br.senai.simuladoprova.repository.PedidoRepository;
import br.senai.simuladoprova.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    // Repositories usados para acessar pedidos e produtos no banco de dados.
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Pedido> listarTodos() {
        // Aqui fica a logica para buscar todos os pedidos cadastrados.
        return pedidoRepository.findAll();
    }

    public Pedido adicionar(PedidoRequestDTO dto) {
        // Aqui criamos uma entidade Pedido com os dados recebidos pela API.
        Pedido pedido = new Pedido();
        pedido.setNomeCliente(dto.getNomeCliente());
        pedido.setStatus(dto.getStatus() == null ? "CRIADO" : dto.getStatus());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setProdutos(buscarProdutos(dto.getProdutoIds()));

        // O save grava o pedido e o relacionamento com seus produtos.
        return pedidoRepository.save(pedido);
    }

    public Pedido alterar(Long id, PedidoRequestDTO dto) {
        // Primeiro buscamos o pedido existente para evitar alterar um id que nao existe.
        Pedido pedido = buscarPorId(id);
        pedido.setNomeCliente(dto.getNomeCliente());
        pedido.setStatus(dto.getStatus());

        // Se a requisicao enviar produtos, tambem atualizamos a lista do pedido.
        if (dto.getProdutoIds() != null) {
            pedido.setProdutos(buscarProdutos(dto.getProdutoIds()));
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido alterarProdutos(Long id, PedidoProdutosRequestDTO dto) {
        // Aqui alteramos somente os produtos vinculados ao pedido.
        Pedido pedido = buscarPorId(id);
        pedido.setProdutos(buscarProdutos(dto.getProdutoIds()));

        return pedidoRepository.save(pedido);
    }

    public void remover(Long id) {
        // Antes de excluir, buscamos o pedido para retornar 404 caso ele nao exista.
        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }

    private Pedido buscarPorId(Long id) {
        // Metodo auxiliar para centralizar a busca de pedido por id.
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido nao encontrado"));
    }

    private List<Produto> buscarProdutos(List<Long> produtoIds) {
        // Um pedido precisa ter ao menos um produto.
        if (produtoIds == null || produtoIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe ao menos um produto");
        }

        // Busca todos os produtos informados na lista de ids.
        List<Produto> produtos = produtoRepository.findAllById(produtoIds);
        if (produtos.size() != produtoIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais produtos nao foram encontrados");
        }

        return produtos;
    }
}

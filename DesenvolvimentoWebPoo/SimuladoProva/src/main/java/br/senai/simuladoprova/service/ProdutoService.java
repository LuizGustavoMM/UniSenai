package br.senai.simuladoprova.service;

import br.senai.simuladoprova.dto.ProdutoRequestDTO;
import br.senai.simuladoprova.model.Produto;
import br.senai.simuladoprova.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    // Repository usado para acessar a tabela de produtos pelo Spring Data JPA.
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodos() {
        // Aqui fica a logica para buscar todos os produtos persistidos.
        return produtoRepository.findAll();
    }

    public Produto adicionar(ProdutoRequestDTO dto) {
        // Aqui convertemos o DTO recebido pela API em uma entidade Produto.
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());

        // O save grava o produto no PostgreSQL e retorna o objeto com id preenchido.
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        // Aqui buscamos um produto pelo id; se nao existir, retornamos erro 404.
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
    }
}

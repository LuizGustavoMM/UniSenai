package br.senai.techshop.service;

import br.senai.techshop.model.Produto;
import br.senai.techshop.repository.ProdutoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> buscarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> buscarProdutoPorCodigo(Long codigo) {
        return produtoRepository.findById(codigo);
    }

}

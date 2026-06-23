package br.senai.LojaProduto.service;

import br.senai.LojaProduto.model.Produto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogisticaService {

    private final ProdutoService produtoService;

    public LogisticaService(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    public String buscarStatusProduto(Long codigo){
        Optional<Produto> produto = produtoService.buscarProdutoPorCodigo(codigo);
        return produto.get().getStatus();
    }

}

package br.senai.simuladoprova.dto;

import java.util.List;

// DTO especifico para alterar somente os produtos de um pedido.
public class PedidoProdutosRequestDTO {

    // Lista de ids que substitui os produtos atuais do pedido.
    private List<Long> produtoIds;

    public List<Long> getProdutoIds() {
        return produtoIds;
    }

    public void setProdutoIds(List<Long> produtoIds) {
        this.produtoIds = produtoIds;
    }
}

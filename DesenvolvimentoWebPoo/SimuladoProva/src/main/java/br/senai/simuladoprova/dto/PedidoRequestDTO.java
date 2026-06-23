package br.senai.simuladoprova.dto;

import java.util.List;

// DTO usado para criar ou alterar um pedido pela API.
public class PedidoRequestDTO {

    // produtoIds guarda apenas os ids dos produtos que devem entrar no pedido.
    private String nomeCliente;
    private String status;
    private List<Long> produtoIds;

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getProdutoIds() {
        return produtoIds;
    }

    public void setProdutoIds(List<Long> produtoIds) {
        this.produtoIds = produtoIds;
    }
}

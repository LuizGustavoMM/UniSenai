package br.senai.techshop.model;

import java.util.List;

public class PedidoRequestDTO {

    private String cliente;
    private String status;
    private List<Long> produtos;

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Long> produtos) {
        this.produtos = produtos;
    }

}

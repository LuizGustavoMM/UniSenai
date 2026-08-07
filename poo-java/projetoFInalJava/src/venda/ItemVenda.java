package venda
        ;

import produtos.Produto;

public class ItemVenda {
    private int id;
    private int vendaId;
    private int produtoId;
    private int quantidade;
    private double precoUnitario;

    public ItemVenda(int id, int vendaId, int produtoId, int quantidade, double precoUnitario) {
        this.id = id;
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }


    public ItemVenda(int vendaId, int produtoId, int quantidade, double precoUnitario) {
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public ItemVenda(int produtoId, int quantidade, double precoUnitario) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }


    public int getId() { return id; }
    public int getVendaId() { return vendaId;}
    public int getProdutoId() {return produtoId;}
    public int getQuantidade() { return quantidade;}
    public double getPrecoUnitario() { return precoUnitario;}

    public void setVendaId(int vendaId) { this.vendaId = vendaId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }
}

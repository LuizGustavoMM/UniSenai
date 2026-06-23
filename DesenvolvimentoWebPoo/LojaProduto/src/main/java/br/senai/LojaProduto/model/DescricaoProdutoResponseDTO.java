package br.senai.LojaProduto.model;

public class DescricaoProdutoResponseDTO {
    
    private String descricao;
    private String categoria;

    public DescricaoProdutoResponseDTO(String descricao, String categoria) {
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    
    
}

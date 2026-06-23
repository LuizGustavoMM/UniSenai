package br.senai.LojaProduto.repository;

import br.senai.LojaProduto.model.DescricaoProdutoResponseDTO;
import br.senai.LojaProduto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findAllByPrecoGreaterThan(Double preco);
    
    @Query("SELECT p FROM Produto p where p.descricao = :descricao")
    List<Produto> buscarPorDescricao(String descricao);
    
    @Query("SELECT new br.senai.LojaProduto.model.DescricaoProdutoResponseDTO(p.descricao, c.titulo) FROM Produto p join p.categoria c")
    List<DescricaoProdutoResponseDTO> buscarDescricaoProduto();

}

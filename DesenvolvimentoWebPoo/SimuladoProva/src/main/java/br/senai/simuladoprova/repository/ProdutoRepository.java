package br.senai.simuladoprova.repository;

import br.senai.simuladoprova.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository responsavel por fazer as operacoes de banco da entidade Produto.
// O JpaRepository ja fornece metodos como save, findAll, findById e delete.
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

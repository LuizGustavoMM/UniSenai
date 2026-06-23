package br.senai.LojaProduto.repository;

import br.senai.LojaProduto.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

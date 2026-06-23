package br.senai.simuladoprova.repository;

import br.senai.simuladoprova.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository responsavel por fazer as operacoes de banco da entidade Pedido.
// O Long indica o tipo do id usado pela entidade.
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}

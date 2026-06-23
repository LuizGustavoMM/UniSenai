package br.senai.travelcorp.repository;

import br.senai.travelcorp.model.StatusViagem;
import br.senai.travelcorp.model.Viagem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViagemRepository extends JpaRepository<Viagem, Long> {

    List<Viagem> findByStatus(StatusViagem status);

}

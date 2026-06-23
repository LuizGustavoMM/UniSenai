package br.senai.travelcorp.controller;

import br.senai.travelcorp.model.AtualizacaoStatusDTO;
import br.senai.travelcorp.model.StatusViagem;
import br.senai.travelcorp.model.Viagem;
import br.senai.travelcorp.service.ViagemService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/viagens")
public class ViagemController {

    private final ViagemService viagemService;

    public ViagemController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Viagem viagem) {
        try {
            Viagem salva = viagemService.cadastrarViagem(viagem);
            return ResponseEntity.status(HttpStatus.CREATED).body(salva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar viagem: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<Viagem> viagens = viagemService.buscarViagens();
            return ResponseEntity.ok(viagens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar viagens: " + e.getMessage());
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
        try {
            Optional<Viagem> viagem = viagemService.buscarViagemPorCodigo(codigo);

            if (viagem.isPresent()) {
                return ResponseEntity.ok(viagem.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar viagem: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> buscarPorStatus(@PathVariable StatusViagem status) {
        try {
            List<Viagem> viagens = viagemService.buscarViagensPorStatus(status);
            return ResponseEntity.ok(viagens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar viagem por status: " + e.getMessage());
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> alterar(@PathVariable Long codigo, @RequestBody Viagem viagem) {
        try {
            Optional<Viagem> atualizada = viagemService.alterarViagem(codigo, viagem);

            if (atualizada.isPresent()) {
                return ResponseEntity.ok(atualizada.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao alterar viagem: " + e.getMessage());
        }
    }

    @PutMapping("/{codigo}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long codigo, @RequestBody AtualizacaoStatusDTO statusDto) {
        try {
            Optional<Viagem> atualizada = viagemService.alterarStatus(codigo, statusDto.getStatus());

            if (atualizada.isPresent()) {
                return ResponseEntity.ok(atualizada.get());
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao alterar status da viagem: " + e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> remover(@PathVariable Long codigo) {
        try {
            boolean removida = viagemService.removerViagem(codigo);

            if (removida) {
                return ResponseEntity.ok("Viagem removida com sucesso!");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Viagem não encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover viagem: " + e.getMessage());
        }
    }

}

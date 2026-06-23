package br.senai.travelcorp.service;

import br.senai.travelcorp.model.StatusViagem;
import br.senai.travelcorp.model.Viagem;
import br.senai.travelcorp.repository.ViagemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ViagemService {

    private final ViagemRepository viagemRepository;

    public ViagemService(ViagemRepository viagemRepository) {
        this.viagemRepository = viagemRepository;
    }

    public Viagem cadastrarViagem(Viagem viagem) {
        return viagemRepository.save(viagem);
    }

    public List<Viagem> buscarViagens() {
        return viagemRepository.findAll();
    }

    public Optional<Viagem> buscarViagemPorCodigo(Long codigo) {
        return viagemRepository.findById(codigo);
    }

    public List<Viagem> buscarViagensPorStatus(StatusViagem status) {
        return viagemRepository.findByStatus(status);
    }

    public Optional<Viagem> alterarViagem(Long codigo, Viagem viagemAtualizada) {
        Optional<Viagem> viagemExistente = viagemRepository.findById(codigo);

        if (viagemExistente.isEmpty()) {
            return Optional.empty();
        }

        Viagem viagem = viagemExistente.get();
        viagem.setColaborador(viagemAtualizada.getColaborador());
        viagem.setDestino(viagemAtualizada.getDestino());
        viagem.setDataInicio(viagemAtualizada.getDataInicio());
        viagem.setDataRetorno(viagemAtualizada.getDataRetorno());
        viagem.setMotivo(viagemAtualizada.getMotivo());
        viagem.setStatus(viagemAtualizada.getStatus());
        return Optional.of(viagemRepository.save(viagem));
    }

    public Optional<Viagem> alterarStatus(Long codigo, StatusViagem status) {
        Optional<Viagem> viagemExistente = viagemRepository.findById(codigo);

        if (viagemExistente.isEmpty()) {
            return Optional.empty();
        }

        Viagem viagem = viagemExistente.get();
        viagem.setStatus(status);
        return Optional.of(viagemRepository.save(viagem));
    }

    public boolean removerViagem(Long codigo) {
        if (!viagemRepository.existsById(codigo)) {
            return false;
        }
        viagemRepository.deleteById(codigo);
        return true;
    }

}

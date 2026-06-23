package br.senai.LojaProduto.service;

import br.senai.LojaProduto.model.Categoria;
import br.senai.LojaProduto.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria cadastrarCategoria(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    Categoria buscarPorCodigo(Long categoria) {
         return categoriaRepository.getReferenceById(categoria);
    }

    public List<Categoria> buscarCategorias() {
        return categoriaRepository.findAll();
    }

}

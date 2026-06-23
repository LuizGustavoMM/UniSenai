package br.senai.LojaProduto.controller;

import br.senai.LojaProduto.service.LogisticaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logistica")
public class LogisticaController {

    private final LogisticaService logisticaService;

    public LogisticaController(LogisticaService logisticaService){
        this.logisticaService = logisticaService;
    }

    @GetMapping("/buscarStatusProduto")
    public ResponseEntity buscarStatusProduto(Long codigoProduto){

        String statusProduto = logisticaService.buscarStatusProduto(codigoProduto);

        return new ResponseEntity(statusProduto, HttpStatus.OK);
    }

}

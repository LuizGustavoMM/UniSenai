package br.senai.LojaProduto;

import br.senai.LojaProduto.controller.ProdutoController;
import br.senai.LojaProduto.service.LogisticaService;
import br.senai.LojaProduto.service.ProdutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LojaProdutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaProdutoApplication.class, args);
	}

}

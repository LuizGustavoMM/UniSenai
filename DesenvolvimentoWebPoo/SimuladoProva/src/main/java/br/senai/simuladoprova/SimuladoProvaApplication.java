package br.senai.simuladoprova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimuladoProvaApplication {

    // Aqui inicia a aplicacao Spring Boot e carrega controllers, services e repositories.
    public static void main(String[] args) {
        SpringApplication.run(SimuladoProvaApplication.class, args);
    }
}

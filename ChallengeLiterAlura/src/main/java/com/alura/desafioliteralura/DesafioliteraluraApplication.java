package com.alura.desafioliteralura;

import com.alura.desafioliteralura.principal.Principal;
import com.alura.desafioliteralura.repositorio.LivrosRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioliteraluraApplication implements CommandLineRunner {
	@Autowired
	private LivrosRepositorio repositorio;

	public static void main(String[] args) {
		SpringApplication.run(DesafioliteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();

	}
}

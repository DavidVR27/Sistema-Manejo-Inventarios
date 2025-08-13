package com.davidvilela.SistemaManejoInventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("entity")
@EnableJpaRepositories("repository")
@ComponentScan({"controller", "service", "security", "exceptions", "config"})
public class SistemaManejoInventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaManejoInventarioApplication.class, args);
	}

}

package com.aivle.miniproject5_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Miniproject5BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Miniproject5BackendApplication.class, args);
	}

}

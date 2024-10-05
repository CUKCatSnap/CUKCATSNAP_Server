package com.cuk.catsnap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CatsnapApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatsnapApplication.class, args);
	}

}

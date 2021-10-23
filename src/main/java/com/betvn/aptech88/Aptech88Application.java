package com.betvn.aptech88;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Aptech88Application {

	public static void main(String[] args) {
		SpringApplication.run(Aptech88Application.class, args);
	}

}

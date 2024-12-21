package com.resilience4j.resililence4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Resililence4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(Resililence4jApplication.class, args);
	}

}

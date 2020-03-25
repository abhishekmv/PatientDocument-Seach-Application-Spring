package com.qpidhealth.qpid.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class SimpleSearchAppSpringbootApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SimpleSearchAppSpringbootApplication.class, args);
	}

}

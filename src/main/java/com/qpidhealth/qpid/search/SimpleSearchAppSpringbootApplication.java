package com.qpidhealth.qpid.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;



@SpringBootApplication
@EnableJpaAuditing
//@EnableSwagger2
public class SimpleSearchAppSpringbootApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SimpleSearchAppSpringbootApplication.class, args);
	}
	
//	@Bean
//    public Docket api() { 
//        return new Docket(DocumentationType.SWAGGER_2)  
//          .select()                                  
//          .apis(RequestHandlerSelectors.any())              
//          .paths(PathSelectors.any())                          
//          .build();                                           
//    }
}

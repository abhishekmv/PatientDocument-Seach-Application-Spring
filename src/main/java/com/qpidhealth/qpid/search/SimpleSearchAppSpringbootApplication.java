package com.qpidhealth.qpid.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
public class SimpleSearchAppSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSearchAppSpringbootApplication.class, args);
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.qpidhealth.qpid.search.controller")) // Generate API of EndPoints which is available inside defined package
                .paths(PathSelectors.any()) // for all EndPoints
                .build(); // create object
    }
}

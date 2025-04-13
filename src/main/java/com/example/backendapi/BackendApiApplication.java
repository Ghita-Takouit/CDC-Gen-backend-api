package com.example.backendapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
<<<<<<< HEAD
@ComponentScan(basePackages = {"com.example.backendapi", "repository", "Controller", "service", "config", "utils"})
=======
@ComponentScan(basePackages = {"com.example.backendapi", "repository", "Controller", "service", "config", "dto"})
>>>>>>> feature-login
@EntityScan("Models")
@EnableJpaRepositories("repository")
public class BackendApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApiApplication.class, args);
    }
}

package com.example.backendapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"Models", "com.example.backendapi.models"})  // Add other model packages if needed
@EnableJpaRepositories(basePackages = {"com.example.backendapi.repositories"})  // Adjust if your repos are in a different package
public class JpaConfig {
    // Configuration is handled by annotations
}

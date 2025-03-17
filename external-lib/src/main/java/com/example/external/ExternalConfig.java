package com.example.external;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalConfig {

    @Bean
    public String externalBean() {
        return "Hello from ExternalConfig!";
    }
}
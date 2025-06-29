package com.banquito.originacion.analisis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Análisis")
                        .version("1.0.0")
                        .description("API para gestionar el historial de estados y observaciones de analistas en el proceso de originación")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("desarrollo@banquito.com")
                                .url("https://banquito.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
} 
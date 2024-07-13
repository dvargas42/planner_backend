package com.dvargas42.planner_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(information());
    }

    private Info information() {
        return new Info().title("Planner Backend")
                .description("API responsible for managing trips")
                .version("1");
    }
}


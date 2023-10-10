package com.example.springbootarticles.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    private static final String SCHEME_NAME = "Access Token";
    private static final String SCHEME = "bearer";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, createSecurityScheme()))
                .servers(Collections.singletonList(new Server().url("https://dzalex.sytes.net/spring-boot-articles")))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)
                .bearerFormat("JWT")
                .scheme(SCHEME);
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("API")
                .pathsToMatch("/api/**")
                .build();
    }

    // Describe the API
    private Info apiInfo() {
        return new Info()
                .title("Articles API")
                .description("Simple Articles Application")
                .contact(new Contact()
                        .email("maksim.dzjubenko@ivkhk.ee")
                        .name("Maksim Dzjubenko"));
    }
}

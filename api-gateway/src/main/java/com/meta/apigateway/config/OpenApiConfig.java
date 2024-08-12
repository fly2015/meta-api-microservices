package com.meta.apigateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth-service")
                .pathsToMatch("/api/v1/auth/**", "/api/v1/jwt/parse")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-service")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }
}
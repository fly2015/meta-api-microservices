package com.meta.apigateway.config;

import com.meta.apigateway.filter.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;
    public SecurityConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf((csrf) -> csrf.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/auth-service/api/v1/auth/**","/api/v1/auth/**", "/ping", "/v3/api-docs/**",
                                        "/v3/api-docs.yaml","/v3/api-docs", "/api-docs/**", "/webjars/**","/swagger-ui.html", "/swagger-ui/**",  "/favicon.ico", "/actuator/**").permitAll()
                                .anyExchange().authenticated()
                )
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
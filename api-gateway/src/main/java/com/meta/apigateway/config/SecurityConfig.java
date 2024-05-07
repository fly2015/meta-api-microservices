package com.meta.apigateway.config;

import com.meta.apigateway.filter.AuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;
    public SecurityConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }
    @Bean
    protected void configure(HttpSecurity http) throws Exception {
         http.csrf((csrf) -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .anyRequest().authenticated()
                ).build();
    }
}
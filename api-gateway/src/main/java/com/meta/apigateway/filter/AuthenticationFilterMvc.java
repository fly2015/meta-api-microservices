package com.meta.apigateway.filter;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilterMvc extends OncePerRequestFilter {
    public static final String HEADER = "Authorization";
    public static final String HEADER_VALUE_PREFIX = "Bearer";
    private static final String JWT_PARSE_URL = "http://auth-service/api/v1/jwt/parse";
    private final RestTemplate restTemplate;

    public AuthenticationFilterMvc(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private Map<String, Object> parseJwt(String token) {
        return restTemplate.exchange(
                JWT_PARSE_URL,
                HttpMethod.POST,
                new HttpEntity<>(Map.of("token", token)),
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        ).getBody();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader(HEADER);
        if (token != null) {
            token = token.replace(HEADER_VALUE_PREFIX + " ", "");
            try {
                Map<String, Object> responseDto = parseJwt(token);
                String username = (String) responseDto.get("username");
                List<String> authorities = (List<String>) responseDto.get("authorities");
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
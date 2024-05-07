package com.meta.apigateway.filter;

import com.meta.apigateway.dto.JwtParseRequestDto;
import com.meta.apigateway.dto.JwtParseResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    public static final String HEADER = "Authorization";
    public static final String HEADER_VALUE_PREFIX = "Bearer";
    private static final String JWT_PARSE_URL = "http://auth-service/v1/jwt/parse";
    private final RestTemplate restTemplate;
    public AuthenticationFilter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HEADER);
        if (token != null) {
            token = token.replace(HEADER_VALUE_PREFIX + " ", "");

            try {
                JwtParseResponseDto responseDto = parseJwt(token);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        responseDto.getUsername(),
                        null,
                        responseDto.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ignore) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private JwtParseResponseDto parseJwt(String token) {
        JwtParseResponseDto responseDto = restTemplate.postForObject(JWT_PARSE_URL, new JwtParseRequestDto(token),
                JwtParseResponseDto.class);

        Objects.requireNonNull(responseDto);
        return responseDto;
    }
}


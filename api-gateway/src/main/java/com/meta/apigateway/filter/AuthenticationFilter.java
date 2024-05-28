package com.meta.apigateway.filter;

import com.meta.apigateway.dto.JwtParseRequestDto;
import com.meta.apigateway.dto.JwtParseResponseDto;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter implements WebFilter {
    public static final String HEADER = "Authorization";
    public static final String HEADER_VALUE_PREFIX = "Bearer";
    private static final String JWT_PARSE_URL = "http://auth-service/api/v1/jwt/parse";
    //private final RestTemplate restTemplate;
    private final WebClient webClient;
    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    private Mono<JwtParseResponseDto> parseJwt(String token) {
        /*JwtParseResponseDto responseDto = webClient.postForObject(JWT_PARSE_URL, new JwtParseRequestDto(token),
                JwtParseResponseDto.class);*/
        return webClient.post()
                .uri(JWT_PARSE_URL)
                .bodyValue(new JwtParseRequestDto(token))
                .retrieve()
                .bodyToMono(JwtParseResponseDto.class);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String token = request.getHeaders().getFirst(HEADER);
        if (token != null) {
            token = token.replace(HEADER_VALUE_PREFIX + " ", "");
            return parseJwt(token).flatMap(responseDto ->
                    {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                responseDto.getUsername(),
                                null,
                                responseDto.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                        );
                        SecurityContextImpl securityContext = new SecurityContextImpl(auth);
                        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                    })
                    .onErrorResume(e -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext()));
        }

        return chain.filter(exchange);

    }
}


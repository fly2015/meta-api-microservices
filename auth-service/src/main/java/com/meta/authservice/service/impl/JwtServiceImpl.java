package com.meta.authservice.service.impl;

import com.meta.authservice.dto.JwtParseRequestDto;
import com.meta.authservice.dto.JwtParseResponseDto;
import com.meta.authservice.security.JwtTokenProvider;
import com.meta.authservice.service.JwtService;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class JwtServiceImpl implements JwtService {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtServiceImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    public JwtParseResponseDto parseJwt(String token) throws ServletException {
        Objects.requireNonNull(token);
        jwtTokenProvider.validateToken(token);
        JwtParseResponseDto responseDto = new JwtParseResponseDto();
        responseDto.setUsername(jwtTokenProvider.getUsername(token));
        responseDto.setAuthorities(jwtTokenProvider.getAuthorities(token));
        return responseDto;
    }
}

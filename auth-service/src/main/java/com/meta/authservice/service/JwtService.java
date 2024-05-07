package com.meta.authservice.service;

import com.meta.authservice.dto.JwtParseRequestDto;
import com.meta.authservice.dto.JwtParseResponseDto;
import jakarta.servlet.ServletException;

public interface JwtService {
    JwtParseResponseDto parseJwt(String token) throws ServletException;
}

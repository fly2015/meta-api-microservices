package com.meta.authservice.controller;

import com.meta.authservice.dto.ErrorDto;
import com.meta.authservice.dto.JwtParseRequestDto;
import com.meta.authservice.dto.JwtParseResponseDto;
import com.meta.authservice.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/jwt")
public class JwtController {
    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @PostMapping("/parse")
     public ResponseEntity<?> parseJwt(@RequestBody JwtParseRequestDto jwtParseRequestDto) {
        try {
            JwtParseResponseDto jwtParseResponseDto = jwtService.parseJwt(jwtParseRequestDto.getToken());
            return new ResponseEntity<>(jwtParseResponseDto, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("JWT parsing error: {}, token: {}", ex.getLocalizedMessage(), jwtParseRequestDto);
            ex.printStackTrace();

            return new ResponseEntity<>(new ErrorDto(ex.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
     }

}
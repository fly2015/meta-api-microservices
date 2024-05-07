package com.meta.authservice.service;

import com.meta.authservice.payload.LoginDto;
import com.meta.authservice.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}

package com.droplite.service;

import com.droplite.dto.UserDto;

public interface IJwtService {
    String generateToken(String username, String role);

    String extractUsername(String token);

    boolean validateToken(String token, UserDto userDto);
}

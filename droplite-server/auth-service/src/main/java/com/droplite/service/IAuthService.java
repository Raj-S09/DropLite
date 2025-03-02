package com.droplite.service;

import com.droplite.dto.LoginRequest;
import com.droplite.dto.LoginResponse;
import com.droplite.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse register(UserDto request);

    void setSessionCookie(String token, HttpServletResponse response);

    void removeSessionCookie(HttpServletResponse response);
}

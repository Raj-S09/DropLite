package com.droplite.controller;

import com.droplite.constant.AuthConstants;
import com.droplite.dto.ApiResponseDto;
import com.droplite.dto.LoginRequest;
import com.droplite.dto.LoginResponse;
import com.droplite.dto.UserDto;
import com.droplite.service.IAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);
        authService.setSessionCookie(loginResponse.getToken(), response);
        return ApiResponseDto.generateResponseEntity(AuthConstants.SUCCESS_MSG_LOGIN_SUCCESSFUL, loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> logout(HttpServletResponse response) {
        authService.removeSessionCookie(response);
        return ApiResponseDto.generateResponseEntity(AuthConstants.SUCCESS_MSG_LOGOUT_SUCCESSFUL, null);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody UserDto request) {
        LoginResponse loginResponse = authService.register(request);
        return ApiResponseDto.generateResponseEntity(AuthConstants.SUCCESS_MSG_LOGIN_SUCCESSFUL, loginResponse);
    }
}
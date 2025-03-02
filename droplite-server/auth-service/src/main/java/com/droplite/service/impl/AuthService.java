package com.droplite.service.impl;

import com.droplite.constant.AuthConstants;
import com.droplite.dto.ApiResponseDto;
import com.droplite.dto.LoginRequest;
import com.droplite.dto.LoginResponse;
import com.droplite.dto.UserDto;
import com.droplite.exception.DropLiteException;
import com.droplite.service.IAuthService;
import com.droplite.service.IJwtService;
import com.droplite.util.InternalApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private static final String COOKIE_SESSION = "_session";

    private final IJwtService jwtService;
    private final InternalApiClient internalApiClient;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Value("${api.url.user}")
    private String userUrl;

    @Override
    public LoginResponse login(LoginRequest request) {
        ApiResponseDto getUserResponse = internalApiClient.callApi(userUrl, HttpMethod.GET,
                Map.of(AuthConstants.KEY_USERNAME, request.getUsername()), null, ApiResponseDto.class);
        if (!HttpStatus.OK.equals(getUserResponse.getStatus()))
            throw new DropLiteException(getUserResponse.getMessage());
        UserDto userDto = objectMapper.convertValue(getUserResponse.getData(), UserDto.class);
        if (!passwordEncoder.matches(request.getPassword(), userDto.getPassword()))
            throw new DropLiteException(AuthConstants.ERROR_MSG_LOGIN_FAILED);
        String token = jwtService.generateToken(userDto.getUsername(), userDto.getRole());
        return new LoginResponse(userDto.getId(), token);
    }

    @Override
    public LoginResponse register(UserDto request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        ApiResponseDto createUserResponse = internalApiClient.callApi(userUrl, HttpMethod.POST,
                null, request, ApiResponseDto.class);
        if (!HttpStatus.OK.equals(createUserResponse.getStatus()))
            throw new DropLiteException(createUserResponse.getMessage());
        UserDto userDto = objectMapper.convertValue(createUserResponse.getData(), UserDto.class);
        String token = jwtService.generateToken(userDto.getUsername(), userDto.getRole());
        return new LoginResponse(userDto.getId(), token);
    }

    @Override
    public void setSessionCookie(String token, HttpServletResponse response) {
        Cookie sessionCookie = new Cookie(COOKIE_SESSION, token);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(60 * 60);
        response.addCookie(sessionCookie);
    }

    @Override
    public void removeSessionCookie(HttpServletResponse response) {
        Cookie sessionCookie = new Cookie(COOKIE_SESSION, null);
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
        SecurityContextHolder.clearContext();
    }
}

package com.droplite.controller;

import com.droplite.constant.UserConstants;
import com.droplite.dto.ApiResponseDto;
import com.droplite.dto.UserDto;
import com.droplite.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseDto> getUser(@RequestParam String username) {
        return ApiResponseDto.generateResponseEntity(
                UserConstants.SUCCESS_MSG_USER_FETCHED, userService.getByUsername(username));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createUser(@RequestBody UserDto userDto) {
        return ApiResponseDto.generateResponseEntity(
                UserConstants.SUCCESS_MSG_USER_CREATED, userService.createUser(userDto));
    }
}
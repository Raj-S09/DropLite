package com.projects.droplite.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseDto<T> {

    private HttpStatus status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ResponseEntity<ApiResponseDto<T>> generateResponseEntity(String message, T data) {
        return ResponseEntity.ok(
                ApiResponseDto.<T>builder()
                        .status(HttpStatus.OK)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}

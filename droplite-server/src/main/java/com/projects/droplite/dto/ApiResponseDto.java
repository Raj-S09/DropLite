package com.projects.droplite.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApiResponseDto {

    private HttpStatus status;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
    private Integer totalPages;
    private Long totalCount;

    public static <T> ResponseEntity<ApiResponseDto> generateResponseEntity(String message, T data) {
        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    public static <T> ResponseEntity<ApiResponseDto> generateResponseEntity(String message, Page<T> data) {
        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(message)
                        .totalPages(data.getTotalPages())
                        .totalCount(data.getTotalElements())
                        .data(data.getContent())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}

package com.droplite.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApiResponseDto {

    private HttpStatus status;
    private int statusCode;
    private String message;
    private Object data;
    private long timestamp;
    private Integer totalPages;
    private Long totalCount;

    public static <T> ResponseEntity<ApiResponseDto> generateResponseEntity(String message, T data) {
        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message(message)
                        .data(data)
                        .timestamp(Instant.now().toEpochMilli())
                        .build()
        );
    }

    public static <T> ResponseEntity<ApiResponseDto> generateResponseEntity(
            String message, T data, int totalPages, long totalElements) {
        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message(message)
                        .totalPages(totalPages)
                        .totalCount(totalElements)
                        .data(data)
                        .timestamp(Instant.now().toEpochMilli())
                        .build()
        );
    }

}

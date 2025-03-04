package com.droplite.exception.handler;

import com.droplite.dto.ApiResponseDto;
import com.droplite.exception.DropLiteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DropLiteException.class)
    @ResponseBody
    public ResponseEntity<ApiResponseDto> handleCustomException(DropLiteException ex) {
        // Log the exception
        log.error(ex.getMessage(), ex);

        // Return a generic error response
        return ResponseEntity.ok().body(
                ApiResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .timestamp(Instant.now().toEpochMilli()).build()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponseDto> handleCustomException(Exception ex) {
        // Log the exception
        log.error(ex.getMessage(), ex);

        // Return a generic error response
        return ResponseEntity.badRequest().body(
                ApiResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Something went wrong")
                        .timestamp(Instant.now().toEpochMilli()).build()
        );
    }
}

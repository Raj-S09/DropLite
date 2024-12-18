package com.projects.droplite.exception.handler;

import com.projects.droplite.dto.ApiResponseDto;
import com.projects.droplite.exception.DropLiteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponseDto<?>> handleCustomException(Exception ex) {
        // Log the exception
        log.error(ex.getMessage(), ex);

        // Return a generic error response
        return ResponseEntity.badRequest().body(
                ApiResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now()).build()
        );
    }
}

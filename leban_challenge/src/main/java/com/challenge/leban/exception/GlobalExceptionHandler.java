package com.challenge.leban.exception;

import com.challenge.leban.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorResponseDto> handleCoreException(CoreException ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            ex.getMessage() != null ? ex.getMessage() : "Error interno del servidor",
            ex.getCodeMessage(),
            ex.getStatusCode(),
            request.getDescription(false).replace("uri=", "")
        );

        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            "Error interno del servidor",
            "exception.internal",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
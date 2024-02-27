package com.martikan.employeeapi.exception;

import com.martikan.employeeapi.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(final NotFoundException e) {
        final var res = new ApiResponse<>(HttpStatus.NOT_FOUND.name(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiResponse<String>> handleBadRequestException(final BadRequestException e) {
        final var res = new ApiResponse<>(HttpStatus.BAD_REQUEST.name(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}

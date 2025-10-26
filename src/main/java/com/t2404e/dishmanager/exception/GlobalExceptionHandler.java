package com.t2404e.dishmanager.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation Failed");
        error.put("details", fieldErrors);
        return error;
    }

    // JSON parse errors (ví dụ: thừa dấu phẩy, sai kiểu dữ liệu)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, Object> handleJsonParse(HttpMessageNotReadableException ex) {
        Map<String, Object> error = new HashMap<>();
        Throwable cause = ex.getMostSpecificCause();

        String message = "Invalid JSON input";
        if (cause instanceof InvalidFormatException) {
            message = "Invalid format: " + cause.getMessage();
        } else if (cause instanceof MismatchedInputException) {
            message = "JSON structure mismatch: " + cause.getMessage();
        } else if (cause != null) {
            message = "JSON parse error: " + cause.getMessage();
        }

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", message);
        return error;
    }

    // BadRequestException (custom)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, Object> handleBadRequest(BadRequestException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());
        return error;
    }

    // ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, Object> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        return error;
    }

    // ConflictException
    @ExceptionHandler(ConflictException.class)
    public Map<String, Object> handleConflict(ConflictException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Conflict");
        error.put("message", ex.getMessage());
        return error;
    }

    // Fallback cho lỗi không xác định
    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleAll(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return error;
    }
}

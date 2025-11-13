package com.example.transmagdalena.api;

import com.example.transmagdalena.utilities.error.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleIllegalException(IllegalArgumentException ex, WebRequest request) {
        var body = ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false),
                List.of());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, WebRequest request){
            var body = ApiError.of(HttpStatus.NOT_FOUND,ex.getMessage(), request.getDescription(false),
                    List.of());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        var violations = ex.getBindingResult().getFieldErrors().stream().map(
                f -> new ApiError.FieldViolation(f.getField(), f.getDefaultMessage())).toList();
        var body =  ApiError.of(HttpStatus.BAD_REQUEST,ex.getMessage(), request.getDescription(false),
                violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        var violations = ex.getConstraintViolations().stream().map(f -> new  ApiError.FieldViolation(f.getPropertyPath().toString(), f.getMessage())).toList();
        var body =  ApiError.of(HttpStatus.BAD_REQUEST, "Constraint violation", request.getDescription(false),violations);
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex, WebRequest req) {
        var body = ApiError.of(HttpStatus.CONFLICT, ex.getMessage(), req.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, WebRequest req) {
        var body = ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}


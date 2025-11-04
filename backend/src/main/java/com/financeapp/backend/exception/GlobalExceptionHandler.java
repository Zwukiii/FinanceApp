package com.financeapp.backend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<Map<String, String>> handleValidException(MethodArgumentNotValidException e) {
        // Stores the errors we get
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(

                error -> {
                    String name = ((FieldError) error).getField();
                    String errorMess = error.getDefaultMessage();
                    errors.put(name, errorMess);
                });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException r) {
        Map<String, String> response = new HashMap<>();
        response.put("error", r.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

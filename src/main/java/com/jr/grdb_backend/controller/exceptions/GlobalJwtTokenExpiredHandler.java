package com.jr.grdb_backend.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalJwtTokenExpiredHandler {
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<String> handleJwtTokenExpiredException(JwtTokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Token Expired: " + ex.getMessage());
    }
}

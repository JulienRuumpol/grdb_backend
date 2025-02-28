package com.jr.grdb_backend.controller.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalJwtTokenExpiredHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleJwtTokenExpiredException(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Token Expired: " + ex.getMessage());
    }
}

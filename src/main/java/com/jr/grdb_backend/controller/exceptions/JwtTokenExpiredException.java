package com.jr.grdb_backend.controller.exceptions;

public class JwtTokenExpiredException  extends RuntimeException {
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}
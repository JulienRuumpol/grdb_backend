package com.jr.grdb_backend.controller.exceptions;

import lombok.Data;

@Data
public class UnauthorizedActionException extends  RuntimeException{
    private final String errorMessage;

    public UnauthorizedActionException(String message) {
        super(message);
        this.errorMessage = message;
    }}

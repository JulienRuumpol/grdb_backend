package com.jr.grdb_backend.controller.exceptions;

public class AlreadyExistingEmailException extends RuntimeException {
    public AlreadyExistingEmailException(String message) {
        super(message);

    }
}

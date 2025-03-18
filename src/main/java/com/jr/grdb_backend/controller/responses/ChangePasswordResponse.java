package com.jr.grdb_backend.controller.responses;

import lombok.Data;

@Data
public class ChangePasswordResponse {
    private int code;

    public ChangePasswordResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private String message;
}

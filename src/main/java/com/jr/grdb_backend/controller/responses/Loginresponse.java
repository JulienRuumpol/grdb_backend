package com.jr.grdb_backend.controller.responses;

import lombok.Data;

@Data
public class Loginresponse {
    private String token;
    private Long expiresIn;

    public Loginresponse(String token, Long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}

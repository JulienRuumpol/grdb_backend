package com.jr.grdb_backend.controller.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Loginresponse {
    private String token;
    private String refreshToken;


    public Loginresponse(String token,  String refreshToken ) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}

package com.jr.grdb_backend.controller.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Loginresponse {
    private String accessToken;
    private String refreshToken;


    public Loginresponse(String accessToken, String refreshToken ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

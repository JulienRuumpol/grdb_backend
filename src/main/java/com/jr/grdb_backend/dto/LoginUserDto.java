package com.jr.grdb_backend.dto;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;
    private String password;
}

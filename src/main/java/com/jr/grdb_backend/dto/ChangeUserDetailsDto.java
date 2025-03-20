package com.jr.grdb_backend.dto;

import lombok.Data;

@Data
public class ChangeUserDetailsDto {

    private String username;
    private String email;
    private String firstname;
    private String lastname;

}

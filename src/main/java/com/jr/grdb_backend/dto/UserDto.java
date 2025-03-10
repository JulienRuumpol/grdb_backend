package com.jr.grdb_backend.dto;

import com.jr.grdb_backend.enume.Language;
import com.jr.grdb_backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Language language;
    private Role role;
}

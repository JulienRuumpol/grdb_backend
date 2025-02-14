package com.jr.grdb_backend.model;


import com.jr.grdb_backend.enume.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class CustomUser {

    @Column(name = "username")
    private String userName;
    private String password;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Id
    private String email;

    @Enumerated(EnumType.STRING)
    private Language language;

}

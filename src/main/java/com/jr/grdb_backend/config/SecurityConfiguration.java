package com.jr.grdb_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //todo set authentication and authorization
        // currently this will allow all requests to go through without authentication
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().permitAll()
//                )
//                .httpBasic(withDefaults());

        http.httpBasic().disable();

        //todo configure csrf
        http.csrf().disable();

        return http.build();

    }
}


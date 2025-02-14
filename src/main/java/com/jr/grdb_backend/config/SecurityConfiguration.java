package com.jr.grdb_backend.config;

import com.jr.grdb_backend.service.impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    @Autowired
    private CustomUserDetailService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //todo set authentication and authorization
        // currently this will allow all requests to go through without authentication

       return http
               .csrf().disable()
//               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(req -> {
//            req.requestMatchers("/login", "/user/register, /swagger-ui/index.html").permitAll();
            req.anyRequest().permitAll();
//            req.requestMatchers("/userRole").hasRole("ADMIN");

        })
               .formLogin(AbstractAuthenticationFilterConfigurer:: permitAll)
               .build();

//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().permitAll()
//                )
//                .httpBasic(withDefaults());

//        http.httpBasic().disable();

        //todo configure csrf
//        http.csrf().disable();
//
//        return http.build();

    }


    @Bean
    public UserDetailsService userDetailsService(){
        return userService;
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(16);
    }
}


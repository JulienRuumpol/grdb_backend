package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.LoginUserDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public CustomUser signUp(RegisterDto dto) {
        CustomUser user = CustomUser.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userName(dto.getUserName())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .enabled(true)
                .build();


        return userRepository.save(user);
    }

    public CustomUser authenticate(LoginUserDto dto){
        CustomUser user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) throw new RuntimeException("User is not enabled");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        return user;
    }
}

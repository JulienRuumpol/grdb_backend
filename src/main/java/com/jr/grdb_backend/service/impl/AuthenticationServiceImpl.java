package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.controller.responses.Loginresponse;
import com.jr.grdb_backend.dto.LoginUserDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.AuthenticationService;
import com.jr.grdb_backend.service.JwtService;
import com.jr.grdb_backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     AuthenticationManager authenticationManager,
                                     UserService userService,
                                     JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public CustomUser register(RegisterDto dto) {
        try{
            Optional<CustomUser> newUser = userRepository.findByEmail(dto.getEmail());
            if (newUser.isPresent()) {
                throw new RuntimeException();
            } else {
                CustomUser user = userService.registerDtoToUser(dto);
                return userRepository.save(user);
            }

        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    public CustomUser authenticate(LoginUserDto dto) {
        try {
            return (CustomUser) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())).getPrincipal();
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public Loginresponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                var authResponse = Loginresponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                return authResponse;
            }
        }
        return null;
    }


}

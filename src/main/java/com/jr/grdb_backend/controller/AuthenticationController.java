package com.jr.grdb_backend.controller;

import com.jr.grdb_backend.controller.responses.Loginresponse;
import com.jr.grdb_backend.dto.LoginUserDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.service.AuthenticationService;
import com.jr.grdb_backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(final JwtService jwtService, final AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomUser> RegisterUser(@RequestBody RegisterDto registerDto) {
        try {
            CustomUser registeredUser = authenticationService.register(registerDto);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomUser());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Loginresponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            CustomUser authenticatedUser = authenticationService.authenticate(loginUserDto);
            String accesToken = jwtService.generateToken(authenticatedUser);
            String refreshToken = jwtService.generateRefreshToken(authenticatedUser);
            Loginresponse loginresponse = new Loginresponse(accesToken,  refreshToken);
            return ResponseEntity.ok(loginresponse);
        } catch (Exception e) {
            Loginresponse loginresponse = new Loginresponse("Invalid user and password combination", "");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginresponse);
        }
    }

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        authenticationService.refreshToken(request, response);
    }

}

package com.jr.grdb_backend.controlller;

import com.jr.grdb_backend.controlller.responses.Loginresponse;
import com.jr.grdb_backend.dto.LoginUserDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.service.JwtService;
import com.jr.grdb_backend.service.impl.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(final JwtService jwtService, final AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<CustomUser> RegisterUser(@RequestBody RegisterDto registerDto) {
        CustomUser registeredUser = authenticationService.signUp(registerDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Loginresponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        CustomUser authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        Loginresponse loginresponse = new Loginresponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginresponse);
    }
}

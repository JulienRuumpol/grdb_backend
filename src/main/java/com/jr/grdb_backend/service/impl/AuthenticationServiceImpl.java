package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.dto.LoginUserDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.repository.UserRepository;
import com.jr.grdb_backend.service.AuthenticationService;
import com.jr.grdb_backend.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     AuthenticationManager authenticationManager,
                                     UserService userService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
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
}

package com.jr.grdb_backend.service;

import com.jr.grdb_backend.dto.LoginUserDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {


     CustomUser register(RegisterDto dto);
     CustomUser authenticate(LoginUserDto dto);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}

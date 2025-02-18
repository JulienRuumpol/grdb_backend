package com.jr.grdb_backend.service;

import com.jr.grdb_backend.dto.LoginUserDto;
import com.jr.grdb_backend.dto.RegisterDto;
import com.jr.grdb_backend.model.CustomUser;

public interface AuthenticationService {


     CustomUser register(RegisterDto dto);
     CustomUser authenticate(LoginUserDto dto);
    }

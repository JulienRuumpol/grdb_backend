package com.jr.grdb_backend.service;

import com.jr.grdb_backend.model.CustomUser;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(CustomUser userDetails);

    String generateToken(Map<String, Object> extraClaims, CustomUser userDetails);

    long getExpirationTime();

    boolean isTokenValid(String token, CustomUser userDetails);

}



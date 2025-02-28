package com.jr.grdb_backend.service.impl;

import com.jr.grdb_backend.model.CustomUser;
import com.jr.grdb_backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.acces_token_expiration_time}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh_token_expiration_time}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(CustomUser userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, CustomUser userDetails) {
        return buildToken(extraClaims, userDetails, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    public String generateRefreshToken(CustomUser userDetails) {
        return buildToken(new HashMap<>(), userDetails, REFRESH_TOKEN_EXPIRATION_TIME);
    }
    

    public long getExpirationTime() {
        return ACCESS_TOKEN_EXPIRATION_TIME;
    }

    private String buildToken(Map<String, Object> extraClaims, CustomUser userDetails, Long expirationTime) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, CustomUser userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
//                .setSigningKey(getSignInKey())
                .setSigningKey(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }


}

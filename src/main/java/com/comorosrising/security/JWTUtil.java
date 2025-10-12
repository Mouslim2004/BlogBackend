package com.comorosrising.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    // 1. Create the secret key for jwt (ps: in a real time app we have to hide this key)
    private static final String SECRET = "b68cf3462c5ccd92ed85f6995cdc4012";
    private static final long EXPIRATION = 86400000;    // Set expiration time
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());  // Signing key derived from secret

    // 2. Create JWT Tokens
    public String generateToken(String email, String role){
        return Jwts.builder()
                .setSubject(email) // user identifier
                .claim("role", role) // user role for authentication
                .setIssuedAt(new Date()) // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 24h expiry
                .signWith(key, SignatureAlgorithm.HS256) // sign with this secret key
                .compact(); // generate final token string
    }

    // 3. Extract email: get user email from token
    public String extractEmail(String token){
        return Jwts.parser()
                .setSigningKey(key) // verifying signature with the same key
                .build()
                .parseClaimsJws(token) // parse and validate token
                .getBody()
                .getSubject(); // extract email from "sub" claim
    }

    // 4. Extract role: get user role from token
    public String extractRole(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // 5. Validate token: check if expired or not
    public boolean isValid(String token){
        try{
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (JwtException e){
            return false;
        }
    }

}

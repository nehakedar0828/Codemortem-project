package com.codemortem.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(
                    "codemortemsecretkeycodemortemsecretkey"
                            .getBytes());

    public String generateToken(String email){

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 60
                        )
                )

                .signWith(
                        SECRET_KEY,
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    //token validation method - from "ajjhdygwb" to "neha@test.com"
    public String extractEmail(String token){

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

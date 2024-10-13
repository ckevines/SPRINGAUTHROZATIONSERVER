// src/main/java/com/example/resourceapp/controller/AuthController.java

package com.example.resourceapp.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AuthController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Token validity in milliseconds (e.g., 1 hour)
    private final long jwtExpirationMs = 3600000;

    @GetMapping("/generate-token")
    public String generateToken() {
        return Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }
}

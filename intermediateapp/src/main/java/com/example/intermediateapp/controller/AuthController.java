// src/main/java/com/example/intermediateapp/controller/AuthController.java

package com.example.intermediateapp.controller;

import com.example.intermediateapp.config.TokenProvider;
import com.example.intermediateapp.util.JwtUtil;
import com.example.intermediateapp.client.ResourceClient;
import com.example.intermediateapp.dto.ResourceResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

// Simplistic authentication controller
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ResourceClient resourceClient;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        // In real applications, validate username and password from DB or another service
        if("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())){
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            // In a real system, token should be returned to the client, not stored server-side
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/call-resource")
    public ResponseEntity<?> callResource(@RequestHeader("Authorization") String authorizationHeader){
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String token = authorizationHeader.substring(7);
        if(!jwtUtil.validateJwtToken(token)){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // Set token in TokenProvider to pass to Feign
        tokenProvider.setToken(token);
        ResourceResponse response = resourceClient.getProtectedResource();
        return ResponseEntity.ok(response);
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class JwtResponse {
        private final String token;
    }
}

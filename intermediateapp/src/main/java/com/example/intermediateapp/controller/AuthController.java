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
            tokenProvider.setToken(token);
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/call-resource")
    public ResponseEntity<?> callResource(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            ResourceResponse response = resourceClient.getProtectedResource();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing");
        }
//        // Ensure that the user is authenticated
//        if(tokenProvider.getToken() == null){
//            return ResponseEntity.status(401).body("Unauthorized");
//        }
//        ResourceResponse response = resourceClient.getProtectedResource();
//        return ResponseEntity.ok(response);
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

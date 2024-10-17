// src/main/java/com/example/intermediateapp/controller/AuthController.java

package com.example.intermediateapp.controller;

import com.example.intermediateapp.config.TokenProvider;
import com.example.intermediateapp.model.LoginRequest;
import com.example.intermediateapp.model.UserDto;
import com.example.intermediateapp.service.AuthenticationService;
import com.example.intermediateapp.util.JwtUtil;
import com.example.intermediateapp.client.ResourceClient;
import com.example.intermediateapp.dto.ResourceResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        return authenticationService.registerUser(userDto);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
//        try {
//            // Attempt to authenticate the user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//            // If authentication is successful, proceed to generate token
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String token = jwtUtil.generateToken(userDetails.getUsername());
//            return ResponseEntity.ok(new JwtResponse(token));
//        } catch (AuthenticationException e) {
//            // If authentication fails, return 401 Unauthorized
//            return ResponseEntity.status(401).body("Invalid credentials");
//        }
//        // In real applications, validate username and password from DB or another service
////        if("user".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())){
////            String token = jwtUtil.generateToken(loginRequest.getUsername());
////            // In a real system, token should be returned to the client, not stored server-side
////            return ResponseEntity.ok(new JwtResponse(token));
////        }
////        return ResponseEntity.status(401).body("Invalid credentials");
//    }

    @PostMapping("/call-resource")
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

//    @Data
//    public static class LoginRequest {
//        private String username;
//        private String password;
//    }
//
//    @Data
//    public static class JwtResponse {
//        private final String token;
//    }


}

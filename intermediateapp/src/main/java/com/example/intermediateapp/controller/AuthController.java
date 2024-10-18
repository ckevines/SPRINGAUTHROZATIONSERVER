// src/main/java/com/example/intermediateapp/controller/AuthController.java

package com.example.intermediateapp.controller;

import com.example.intermediateapp.config.TokenProvider;
import com.example.intermediateapp.model.LoginRequest;
import com.example.intermediateapp.model.User;
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
import javax.validation.Valid;

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
    public ResponseEntity<?> register(@RequestBody User user) {
        return authenticationService.registerUser(user);
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

    @PostMapping("/authenticate-or-call")
    public ResponseEntity<?> authenticateOrCall(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody(required = false) LoginRequest loginRequest) {

        // 1. Authorization 헤더가 존재하고 Bearer 토큰인지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);  // Bearer 다음의 실제 토큰 값 추출
            if (!jwtUtil.validateJwtToken(token)) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid Token");
            }

            // Feign Client에서 사용할 수 있도록 토큰 설정
            tokenProvider.setToken(token);
            ResourceResponse response = resourceClient.getProtectedResource();
            return ResponseEntity.ok(response);
        }

        // 2. LoginRequest 바디가 존재하는지 확인 (로그인 요청 처리)
        if (loginRequest != null) {
            return authenticationService.authenticate(loginRequest);
        }

        // 3. Authorization 헤더와 LoginRequest 둘 다 없는 경우
        return ResponseEntity.status(400).body("Bad Request: Missing Authorization Header or Login Request Body");
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

// src/main/java/com/example/intermediateapp/controller/AuthController.java

package com.example.intermediateapp.controller;

import com.example.intermediateapp.config.TokenProvider;
import com.example.intermediateapp.model.LoginRequest;
import com.example.intermediateapp.model.User;
import com.example.intermediateapp.service.AuthenticationService;
import com.example.intermediateapp.util.JwtUtil;
import com.example.intermediateapp.client.ResourceClient;
import com.example.intermediateapp.dto.ResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Simplistic authentication controller
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final TokenProvider tokenProvider;
    private final ResourceClient resourceClient;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Retrieve the previous token for this user
            String oldToken = tokenProvider.getPreviousTokenForUser(loginRequest.getUsername());

            // Invalidate the old token (if exists)
            if (oldToken != null) {
                tokenProvider.clearToken(oldToken);  // Add old token to blacklist
            }


//            tokenProvider.setToken(newJwt);
//
//            return ResponseEntity.ok(new LoginResponse(newJwt));
            tokenProvider.logCurrentState();
            return authenticationService.authenticate(loginRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
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
        if(tokenProvider.isTokenBlacklisted(token)){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String username = jwtUtil.getUsernameFromToken(token);
        tokenProvider.setTokenForUser(username, token);
        // Set token in TokenProvider to pass to Feign
//        tokenProvider.setToken(token);
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
            if(tokenProvider.isTokenBlacklisted(token)){
                return ResponseEntity.status(401).body("Unauthorized");
            }
            String username = jwtUtil.getUsernameFromToken(token);
            tokenProvider.setTokenForUser(username, token);
            // Feign Client에서 사용할 수 있도록 토큰 설정
//            tokenProvider.setToken(token);
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

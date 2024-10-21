package com.example.intermediateapp.service;

import com.example.intermediateapp.model.JwtResponse;
import com.example.intermediateapp.model.LoginRequest;
import com.example.intermediateapp.model.User;
import com.example.intermediateapp.repository.UserRepository;
import com.example.intermediateapp.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                                 UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        // Authenticate username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // If authentication is successful, set the authenticated user in the context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = jwtUtil.generateToken(authentication.getName());
        return ResponseEntity.ok(new JwtResponse(token, authentication.getName()));
    }

    @Transactional
    public ResponseEntity<?> registerUser(User user) {
        // Check if userSave already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(400).body("User already exists");
        }

        // Create new userSave and hash the password
        User userSave = new User();
        userSave.setUsername(user.getUsername());
        userSave.setPassword(passwordEncoder.encode(user.getPassword()));
        userSave.setRoles(user.getRoles());

        // Save userSave to the database
        userRepository.save(userSave);
        return ResponseEntity.ok("User registered successfully");
    }
}

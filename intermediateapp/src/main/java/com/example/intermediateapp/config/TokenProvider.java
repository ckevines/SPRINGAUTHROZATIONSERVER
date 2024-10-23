package com.example.intermediateapp.config;

import com.example.intermediateapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TokenProvider {

//    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
//
//    public void setToken(String token){
//        tokenHolder.set(token);
//    }
//
//    public String getToken(){
//        return tokenHolder.get();
//    }
//
//    public void clearToken(){
//        tokenHolder.remove();
//    }

    // Map to store the user's latest token
    private ConcurrentHashMap<String, String> userTokenMap = new ConcurrentHashMap<>();
    // Map to store blacklisted tokens and their expiration times (in milliseconds)
    private ConcurrentHashMap<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    private static final long CLEANUP_INTERVAL = 60; // Interval to clean up expired tokens (in seconds)
    private final JwtUtil jwtUtil;

    public TokenProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        // Schedule a task to clean up expired tokens periodically
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::cleanupExpiredTokens, CLEANUP_INTERVAL, CLEANUP_INTERVAL, TimeUnit.SECONDS);
    }

    // Clean up expired tokens
    private void cleanupExpiredTokens() {
        long now = Instant.now().toEpochMilli();
        // Remove tokens from the blacklist if they are expired
        tokenBlacklist.entrySet().removeIf(entry -> {
            String token = entry.getKey();
            Long expirationTime = entry.getValue();

            // If the token has expired, remove it
            boolean isExpired = expirationTime < now || isTokenExpired(token);
            if (isExpired) {
                log.info("Removing expired token from blacklist: {}", token);
            }
            return isExpired;
        });
        log.info("Expired tokens cleanup complete");
    }
    // Store the new token for the user
    public void setTokenForUser(String username, String token) {
        userTokenMap.put(username, token);  // Track the latest token for the user
        log.info("New token set for user {}: {}", username, token);  // Log the new token

    }

    // Retrieve the previous token for the user
    public String getPreviousTokenForUser(String username) {
        String previousToken = userTokenMap.get(username);
        log.info("Previous token for user {}: {}", username, previousToken);  // Log the previous token
        return previousToken;
    }

    // Invalidate the token (add to blacklist)
    public void clearToken(String token) {
        // Extract expiration time from the token
        Date expirationDate = extractExpiration(token);
        long expirationTime = expirationDate.getTime();

        // Add token to the blacklist with the expiration time
        tokenBlacklist.put(token, expirationTime);
//        tokenBlacklist.put(token, true);  // Mark the token as invalid
        log.info("Token blacklisted: {} (expires at {})", token, expirationDate);
    }

    // Check if a token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        Long expirationTime = tokenBlacklist.get(token);

        // If token is not blacklisted or has expired, return false
        if (expirationTime == null || expirationTime < Instant.now().toEpochMilli()) {
            return false;
        }

        // Otherwise, it's blacklisted
        log.info("Token is blacklisted: {}", token);
        return true;

//        boolean isBlacklisted = tokenBlacklist.containsKey(token);
//        log.info("Checking if token is blacklisted: {} -> {}", token, isBlacklisted);  // Log the blacklist check
//        return isBlacklisted;
    }
    // Method to check if the token has expired
    public boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date()); // Check if current date is after the expiration date
    }
    // Method to extract expiration date from the token
    public Date extractExpiration(String token) {
        return jwtUtil.extractAllClaims(token).getExpiration();
    }
    public void logCurrentState() {
        log.info("Current userTokenMap:({}) {}", userTokenMap.size(), userTokenMap.toString());  // Log the entire token map
        log.info("Current tokenBlacklist:({}) {}", tokenBlacklist.size(),tokenBlacklist.toString());  // Log the entire blacklist
    }
}


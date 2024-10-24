//package com.example.intermediateapp.filter;
//
//import com.example.intermediateapp.config.TokenProvider;
//import com.example.intermediateapp.util.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationAndDuplicateRequestFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final TokenProvider tokenProvider;
//
//    // Map to store request fingerprints and their corresponding timestamps
//    private final Map<String, Long> requestMap = new ConcurrentHashMap<>();
//
//    @Value("${filter.requestCheckTime}")
//    private int requestCheckTime;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String token = parseJwt(request);
//        if (token != null && jwtUtil.validateJwtToken(token)) {
//            if (!tokenProvider.isTokenBlacklisted(token)) {
//                // Token is valid and not blacklisted - proceed with authentication
//                String username = jwtUtil.getUsernameFromToken(token);
//
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(username, null, null);
//
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
////                tokenProvider.setToken(token);
//                tokenProvider.setTokenForUser(username, token);
//            } else {
//                // Token is blacklisted - reject the request
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//        }
//
//        // Generate a unique fingerprint for the request
//        String requestFingerprint = generateRequestFingerprint(request);
//
//        // Check if the request is a duplicate
//        if (isDuplicateRequest(requestFingerprint)) {
//            response.sendError(HttpServletResponse.SC_CONFLICT, "Duplicate request detected");
//            return;
//        }
//
//        try {
//            filterChain.doFilter(request, response);
//        } finally {
//            if (token != null) {
//                tokenProvider.clearToken(token);
//            }
//        }
//    }
//
//    private String parseJwt(HttpServletRequest request){
//        String headerAuth = request.getHeader("Authorization");
//
//        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
//            return headerAuth.substring(7);
//        }
//        return null;
//    }
//
//    private String generateRequestFingerprint(HttpServletRequest request) {
//        // You can customize this to include specific parameters or headers
//        return request.getMethod() + ":" + request.getRequestURI() + ":" + request.getQueryString();
//    }
//
//    private boolean isDuplicateRequest(String requestFingerprint) {
//        long currentTime = System.currentTimeMillis();
//        Long lastRequestTime = requestMap.putIfAbsent(requestFingerprint, currentTime);
//
//        if (lastRequestTime != null) {
//            long requestInterval = currentTime - lastRequestTime;
//            log.info("currentTime: ({}) ({})", currentTime, lastRequestTime);
//            log.info("requestInterval: ({}) ", requestInterval);
//            // Check if the request was made within the last 5 seconds (or your desired time window)
//            if (requestInterval < requestCheckTime) {
//                return true;
//            } else {
//                // Update the timestamp to the current time
//                requestMap.put(requestFingerprint, currentTime);
//            }
//        }
//
//        return false;
//    }
//}

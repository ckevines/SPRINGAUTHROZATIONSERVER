// src/main/java/com/example/intermediateapp/filter/AuthTokenFilter.java

package com.example.intermediateapp.filter;

import com.example.intermediateapp.util.AuthTokenHolder;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            AuthTokenHolder.setToken(authHeader);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            AuthTokenHolder.clear();
        }
    }
}

package com.example.intermediateapp.config;

import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public void setToken(String token){
        tokenHolder.set(token);
    }

    public String getToken(){
        return tokenHolder.get();
    }

    public void clearToken(){
        tokenHolder.remove();
    }
}


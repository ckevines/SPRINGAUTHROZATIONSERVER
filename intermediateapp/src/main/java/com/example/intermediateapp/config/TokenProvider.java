package com.example.intermediateapp.config;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {

    // This is a simplistic approach. In real-world scenarios, manage the token with better scope handling.
    private String token;

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}

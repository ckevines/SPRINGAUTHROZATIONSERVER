// src/main/java/com/example/intermediateapp/config/FeignClientConfig.java

package com.example.intermediateapp.config;

import com.example.intermediateapp.util.AuthTokenHolder;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor authRequestInterceptor() {
        return requestTemplate -> {
            // Retrieve the Authorization header from the incoming request
            // and set it to the outgoing Feign request
            // This requires access to the current HTTP request
            // Use ThreadLocal via a Filter to store the token
            String token = AuthTokenHolder.getToken();
            if (token != null) {
                requestTemplate.header("Authorization", token);
            }
        };
    }
}

// src/main/java/com/example/intermediateapp/config/FeignClientConfig.java

package com.example.intermediateapp.config;

import com.example.intermediateapp.util.CustomResourceResponseDecoder;
import com.example.intermediateapp.util.CustomTextDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Configuration
public class FeignClientConfig {

    private final TokenProvider tokenProvider;

    public FeignClientConfig(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return new RequestInterceptor() {
//            @Override
//            public void apply(RequestTemplate template) {
//                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//                if (attributes != null) {
//                    HttpServletRequest request = attributes.getRequest();
//                    String authorization = request.getHeader("Authorization");
//                    if (authorization != null) {
//                        template.header("Authorization", authorization);
//                    }
//                }
//            }
//        };
//    }

    @Bean
    public RequestInterceptor jwtRequestInterceptor(){
        return requestTemplate -> {
            String token = tokenProvider.getToken();
            if(token != null){
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }

    @Bean
    public StringHttpMessageConverter stringConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.TEXT_PLAIN,
                MediaType.APPLICATION_JSON, // If you also expect JSON
                MediaType.ALL // For all other media types if necessary
        ));
        return converter;
    }

    // Optional: If you also need to handle JSON responses
    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public Decoder feignDecoder() {
        return new CustomTextDecoder();
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new CustomResourceResponseDecoder(objectMapper);
    }
}

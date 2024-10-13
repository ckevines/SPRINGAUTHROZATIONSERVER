//// src/main/java/com/example/clientapp/config/FeignClientConfig.java
//
//package com.example.clientapp.config;
//
//import feign.RequestInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class FeignClientConfig {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Bean
//    public RequestInterceptor authRequestInterceptor() {
//        return requestTemplate -> {
//            // Fetch the token from the Resource app's token endpoint
//            String token = restTemplate.getForObject("<http://localhost:8082/generate-token>", String.class);
//            if (token != null) {
//                requestTemplate.header("Authorization", "Bearer " + token);
//            }
//        };
//    }
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//}

package com.example.clientapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableFeignClients
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

//    @Bean
//    RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        String apiPrefix = "/api/";
//        return builder.routes()
//                .route(rs -> rs
//                        .path(apiPrefix + "**")
//                        .filters(f -> f
//                                .tokenRelay()
//                                .rewritePath(apiPrefix + "(?<segment>.*)", "/${segment}")
//                        )
//                        .uri("http://localhost:8081"))
//                .route(rs -> rs
//                        .path("/**")
//                        .uri("http://localhost:8020")
//                )
//                .build();
//    }
//
//    @Bean
//    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange((authorize) -> authorize.anyExchange().authenticated())
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .oauth2Login(Customizer.withDefaults())
//                .oauth2Client(Customizer.withDefaults());
//        return http.build();
//    }

    
}
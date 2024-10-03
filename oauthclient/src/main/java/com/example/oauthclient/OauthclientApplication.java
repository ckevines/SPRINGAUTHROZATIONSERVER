package com.example.oauthclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.Customizer;

@SpringBootApplication
public class OauthclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthclientApplication.class, args);
	}

	@Bean
	RouteLocator routeLocator(RouteLocatorBuilder builder) {
		var apiPrefix = "/api/";
        return builder.routes()
				.route(rs -> rs
						.path(apiPrefix + "**")
						.filters(f -> f
								.tokenRelay()
								.rewritePath(apiPrefix + "(?<segment>.*)", "/$\\{segment}")
						)
						.uri("http://localhost:8081"))
				.route(rs -> rs
						.path("/**")
						.uri("http://localhost:8020")
				)
				// .route(rs -> rs
                //         .path("/**")
                //         .uri("http://localhost:8020")
                // )
//                .route(r -> r.path("/api/orders/**")
//                        .uri("lb://ORDER-SERVICE"))
//                .route(r -> r.path("/api/products/**")
//                        .uri("lb://PRODUCT-SERVICE"))
//                .route(r -> r.path("/api/users/**")
//                        .uri("lb://USER-SERVICE"))
//                .route(r -> r.path("/api/carts/**")
//                        .uri("lb://CART-SERVICE"))
//                .route(r -> r.path("/api/payments/**")
//                        .uri("lb://PAYMENT-SERVICE"))
//                .route(r -> r.path("/api/reviews/**")
//                        .uri("lb://REVIEW-SERVICE"))
//                .route(r -> r.path("/api/addresses/**")
//                        .uri("lb://ADDRESS-SERVICE"))
//                .route(r -> r.path("/api/coupons/**")
//                        .uri("lb://COUPON-SERVICE"))
//                .route(r -> r.path("/api/wishlists/**")
//						 .uri("lb://WISHLIST-SERVICE"))
				.build();
	}

	@Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange((authorize) -> authorize.anyExchange().authenticated())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }

}

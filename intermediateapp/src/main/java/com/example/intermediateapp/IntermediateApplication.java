package com.example.intermediateapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IntermediateApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntermediateApplication.class, args);
	}

	// @Bean
	// InMemoryUserDetailsManager userDetailsService() {
	// 	User.UserBuilder userBuilder = User.builder();
	// 	org.springframework.security.core.userdetails.UserDetails one = userBuilder.roles("admin","user").username("one").password("{noop}pw").build();
	// 	org.springframework.security.core.userdetails.UserDetails two = userBuilder.roles("user").username("two").password("{noop}pw").build();
    //     return new InMemoryUserDetailsManager(one, two);
    // }

//	@Bean
//InMemoryUserDetailsManager userDetailsService() {
//    PasswordEncoder encoder = new BCryptPasswordEncoder();
//    UserDetails one = User.builder()
//        .username("one")
//        .password(encoder.encode("pw"))
//        .roles("admin", "user")
//        .build();
//    UserDetails two = User.builder()
//        .username("two")
//        .password(encoder.encode("pw"))
//        .roles("user")
//        .build();
//    return new InMemoryUserDetailsManager(one, two);
//}

}

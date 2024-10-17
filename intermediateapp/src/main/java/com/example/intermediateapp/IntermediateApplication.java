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


}

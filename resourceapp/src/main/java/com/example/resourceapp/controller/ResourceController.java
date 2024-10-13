// src/main/java/com/example/resourceapp/controller/ResourceController.java

package com.example.resourceapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/api/data")
    public String getProtectedData() {
        return "This is protected data from the Resource Application.";
    }
}

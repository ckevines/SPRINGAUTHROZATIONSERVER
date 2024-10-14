// src/main/java/com/example/resourceapp/controller/ResourceController.java

package com.example.resourceapp.controller;

import com.example.resourceapp.dto.ResourceResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/resource")
    public ResponseEntity<ResourceResponse> getProtectedResource() {
        ResourceResponse response = new ResourceResponse();
        response.setMessage("This is a protected resource.");
        // populate the response object
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }
}

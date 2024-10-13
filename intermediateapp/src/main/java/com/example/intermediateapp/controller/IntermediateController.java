//// src/main/java/com/example/intermediateapp/controller/IntermediateController.java
//
//package com.example.intermediateapp.controller;
//
//import com.example.intermediateapp.client.ResourceClient;
//import com.example.intermediateapp.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class IntermediateController {
//
//    @Autowired
//    private ResourceClient resourceClient;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @GetMapping("/forward/data")
//    public String forwardData() {
//        return resourceClient.getProtectedData();
//    }
//
//    @GetMapping("/generate-token")
//    public String generateToken(@RequestParam String username) {
//        // Generate the token
//        String token = jwtUtil.generateToken(username);
//        return token;
//    }
//
//    @GetMapping("/fetch-protected-data")
//    public String fetchProtectedData(@RequestParam String username) {
//        // Generate the token
//        String token = jwtUtil.generateToken(username);
//        // Call Resource application with the token
//        return resourceClient.getData("Bearer " + token);
//    }
//}

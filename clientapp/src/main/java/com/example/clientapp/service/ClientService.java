// src/main/java/com/example/clientapp/service/ClientService.java

package com.example.clientapp.service;

//import com.example.clientapp.client.IntermediateClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ClientService {
//
//    @Autowired
//    private IntermediateClient intermediateClient;
//
//    public String fetchData() {
//        return intermediateClient.getForwardedData();
//    }
//}

import com.example.clientapp.model.UserDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {
    private final String INTERMEDIATE_URL = "http://localhost:8082";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ClientService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Method to get the token and extract it from the JSON response
    public String getToken(UserDto userDto) {
        // Call the login endpoint to get the token response as a JSON string
        String jsonResponse = restTemplate.postForObject(INTERMEDIATE_URL + "/auth/login", userDto, String.class);

        // Parse the JSON string to extract the token
        try {
            // Use Jackson's ObjectMapper to parse the JSON
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            // Extract the "token" field from the JSON
            return jsonNode.get("token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing token from response: " + jsonResponse, e);
        }
    }

    public String fetchProtectedData(UserDto userDto) {
        // Generate the token
        String token = getToken(userDto);

        // Set the Authorization header with the token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Call Intermediate to fetch protected data
        ResponseEntity<String> response = restTemplate.postForEntity(INTERMEDIATE_URL + "/auth/call-resource", entity, String.class);

        return response.getBody();
    }
}


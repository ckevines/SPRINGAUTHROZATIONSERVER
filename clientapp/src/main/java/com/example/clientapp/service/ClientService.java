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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {

    @Autowired
    private RestTemplate restTemplate;

    private final String INTERMEDIATE_URL = "http://localhost:8082";

    public String getToken(String username) {
        return restTemplate.getForObject(INTERMEDIATE_URL + "/generate-token?username=" + username, String.class);
    }

    public String fetchProtectedData(String username) {
        // Generate the token
        String token = getToken(username);
        // Call Intermediate to fetch protected data
        return restTemplate.getForObject(INTERMEDIATE_URL + "/fetch-protected-data?username=" + username, String.class);
    }
}


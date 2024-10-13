// src/main/java/com/example/intermediateapp/client/ResourceClient.java

package com.example.intermediateapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "resource-client", url = "${resource.app.url}")
public interface ResourceClient {

    @GetMapping("/api/data")
    String getProtectedData();

    @GetMapping("/get-data")
    String getData(@RequestHeader("Authorization") String token);
}

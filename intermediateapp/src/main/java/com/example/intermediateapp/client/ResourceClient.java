// src/main/java/com/example/intermediateapp/client/ResourceClient.java

package com.example.intermediateapp.client;

import com.example.intermediateapp.config.FeignClientConfig;
import com.example.intermediateapp.dto.ResourceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "resourcesapp", url = "${resource.app.url}", configuration = FeignClientConfig.class)
public interface ResourceClient {

    @GetMapping("/resource")
    ResourceResponse getProtectedResource();
}

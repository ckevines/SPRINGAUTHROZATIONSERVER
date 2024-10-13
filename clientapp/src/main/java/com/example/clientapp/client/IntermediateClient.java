// src/main/java/com/example/clientapp/client/IntermediateClient.java

package com.example.clientapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "intermediate-client", url = "${intermediate.app.url}")
public interface IntermediateClient {

    @GetMapping("/forward/data")
    String getForwardedData();
}

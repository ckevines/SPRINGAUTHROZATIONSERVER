// src/main/java/com/example/clientapp/controller/ClientController.java

package com.example.clientapp.controller;

//import com.example.clientapp.service.ClientService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class ClientController {
//
//    @Autowired
//    private ClientService clientService;
//
//    @GetMapping("/get-data")
//    public String getData() {
//        return clientService.fetchData();
//    }
//}
import com.example.clientapp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/fetch-data")
    public String fetchData(@RequestParam String username) {
        return clientService.fetchProtectedData(username);
    }
}

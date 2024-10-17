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
import com.example.clientapp.model.UserDto;
import com.example.clientapp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/fetch-data")
    public String fetchData(@RequestBody UserDto userDto) {
        return clientService.fetchProtectedData(userDto);
    }
}

package com.meta.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        // this is a ping controller that returns 200 OK with json response
        return "Successful!";
    }
}

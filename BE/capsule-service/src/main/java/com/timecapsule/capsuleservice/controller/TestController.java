package com.timecapsule.capsuleservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/capsule")
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "This is capsule-service";
    }
}
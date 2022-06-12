package com.miu.ea.goodpeople.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestResource {

    @GetMapping("/test")
    public String test() {
        return "Hello from Good People Service!";
    }
}
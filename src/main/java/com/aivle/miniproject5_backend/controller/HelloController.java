package com.aivle.miniproject5_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/greet")
    public String greet(@RequestParam(required = false, defaultValue = "en") String lang) {
        if(lang.equals("ko")) {
            return "안녕하세요!";
        } else {
            return "Hello!";
        }
    }
}

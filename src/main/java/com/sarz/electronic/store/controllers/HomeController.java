package com.sarz.electronic.store.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
//@SecurityRequirement(name = "Scheme1")

public class HomeController {
    @GetMapping
    public String test(){
        return "Welcome to my Electronic Store";
    }
}

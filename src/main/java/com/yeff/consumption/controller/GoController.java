package com.yeff.consumption.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GoController {
    @GetMapping(value = "go")
    public String test(){
        return "200 consumption success";
    }
}

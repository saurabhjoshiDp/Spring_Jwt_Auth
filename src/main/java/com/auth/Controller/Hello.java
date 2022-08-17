package com.auth.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @GetMapping("/")
    public String hello(){
        return "<h1>Hello</h1>";
    }
    @GetMapping("/user")
    public String helloUser(){
        return "<h1>Hello User</h1>";
    }
    @GetMapping("/admin")
    public String helloAdmin(){
        return "<h1>Hello Admin</h1>";
    }
}
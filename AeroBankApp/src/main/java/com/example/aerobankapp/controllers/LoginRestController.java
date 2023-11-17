package com.example.aerobankapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRestController
{
    @GetMapping("/login")
    public String login()
    {
        return "login.html";
    }
}

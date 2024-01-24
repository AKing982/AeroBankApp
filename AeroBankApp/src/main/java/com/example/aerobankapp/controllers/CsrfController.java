package com.example.aerobankapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/csrf")
public class CsrfController
{
    @GetMapping("/token")
    @CrossOrigin(origins = "http://localhost:3000")
    public CsrfToken csrfToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}

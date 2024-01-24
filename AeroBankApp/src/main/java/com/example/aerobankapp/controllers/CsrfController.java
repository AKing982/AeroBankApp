package com.example.aerobankapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/csrf", method=RequestMethod.GET)
public class CsrfController
{
    @GetMapping(value ="/token")
    @CrossOrigin(origins = "http://localhost:3000")
    public CsrfToken csrfToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}

package com.example.aerobankapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/api/email")
@CrossOrigin(origins="http://localhost:3000")
public class EmailController
{
    @GetMapping("/data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getEmailSettings()
    {
        return ResponseEntity.ok("Post");
    }
}

package com.example.aerobankapp.controllers;

import com.example.aerobankapp.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/api/email")
@CrossOrigin(origins="http://localhost:3000")
public class EmailController
{
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService)
    {
        this.emailService = emailService;
    }

    @GetMapping("/data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getEmailSettings()
    {
        return ResponseEntity.ok("Post");
    }
}

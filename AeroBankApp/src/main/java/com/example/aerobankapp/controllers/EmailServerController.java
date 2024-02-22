package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.EmailServerEntity;
import com.example.aerobankapp.services.EmailServerService;
import com.example.aerobankapp.workbench.utilities.EmailServerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/email")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailServerController {
    private final EmailServerService emailService;

    @Autowired
    public EmailServerController(EmailServerService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmailServerEntity> addEmailServer(@RequestBody EmailServerRequest emailServer)
    {
        return null;
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmailServerEntity> updateEmailServer(@PathVariable Long id)
    {
        return null;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmailServerEntity> getEmailServerById(@PathVariable Long id)
    {
        return null;
    }

    @PostMapping("/testConnection")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> testConnection(@RequestBody EmailServerEntity emailServer)
    {
        return null;
    }


}

package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.EmailServerEntity;
import com.example.aerobankapp.services.EmailServerService;
import com.example.aerobankapp.workbench.utilities.EmailServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/email")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailServerController {
    private final EmailServerService emailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private Logger LOGGER = LoggerFactory.getLogger(EmailServerController.class);

    @Autowired
    public EmailServerController(EmailServerService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addEmailServer(@RequestBody EmailServerRequest request)
    {
        // Extract the Email Server details from the request
        final String host = request.getHost().trim();
        final int port = request.getPort();
        final String password = request.getPassword().trim();
        final String username = request.getUsername().trim();

        // Encrypt the password
        String encryptedPassword = bCryptPasswordEncoder.encode(password);

        // Convert the Email Server request to a Email Server Entity
        EmailServerEntity serverEntity = buildEmailServerEntity(host, port, username, encryptedPassword);

        // Validate whether a email server connection already exists in the database
        boolean emailServerExists = emailService.emailServerExists(host, port);
        LOGGER.info("Email Server Already exists with host and port: " + emailServerExists);

        if(emailServerExists)
        {
            // Update the record instead of creating a new record
            emailService.update(serverEntity);
        }
        else
        {
            emailService.save(serverEntity);
        }

        return ResponseEntity.ok("Email Server Details have been saved to the database....");
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

    private EmailServerEntity buildEmailServerEntity(String host, int port, String username, String password)
    {
        return EmailServerEntity.builder()
                .port(port)
                .host(host)
                .username(username)
                .password(password)
                .isTLS(false)
                .id(1L)
                .build();
    }


}

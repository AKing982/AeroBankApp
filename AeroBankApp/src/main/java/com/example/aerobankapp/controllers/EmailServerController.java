package com.example.aerobankapp.controllers;

import com.example.aerobankapp.email.EmailConfig;
import com.example.aerobankapp.email.EmailService;
import com.example.aerobankapp.email.EmailServiceImpl;
import com.example.aerobankapp.entity.EmailServerEntity;
import com.example.aerobankapp.services.EmailServerService;
import com.example.aerobankapp.workbench.utilities.EmailServerRequest;
import com.example.aerobankapp.workbench.utilities.TestEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> getEmailServerById(@PathVariable Long id)
    {
        List<EmailServerEntity> entity = emailService.getEmailServerById(id);
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/testVerify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyTestConnection()
    {
        return null;
    }

    @PostMapping("/testConnection")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> testConnection(@RequestBody TestEmailRequest request)
    {
        final String recipient = request.getTestEmail();
        final String sender = request.getFromEmail();

        // Get the Mail Server Settings
        List<EmailServerEntity> mailServerSettings = emailService.getEmailServerById(1L);

        // Get the record from the list
        EmailServerEntity emailServer = mailServerSettings.get(0);

        // Build the Email Configuration
      //  EmailConfig emailConfig = new EmailConfig(emailServer.getHost(), emailServer.getPort(),
             //   emailServer.getUsername(), emailServer.getPassword(),
          //      emailServer.isUseTLS(), emailServer.isAuthRequired());


        // Get the Email Service implementation
      //  EmailService emailService1 = new EmailServiceImpl(emailConfig, null);

        // Create the Test Email
        final String subject = "Test Email";
        final String message = "Test Email Sent.";

       // emailService1.sendEmail(recipient, sender, message, subject);
        return ResponseEntity.ok("Test Connection successful");
    }

    private EmailConfig buildEmailConfig(String host, int port, String username, String password)
    {
        return EmailConfig.builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .useTLS(false)
                .build();
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

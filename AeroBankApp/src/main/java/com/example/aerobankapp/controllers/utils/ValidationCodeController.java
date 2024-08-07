package com.example.aerobankapp.controllers.utils;

import com.example.aerobankapp.email.Email;
import com.example.aerobankapp.email.EmailService;
import com.example.aerobankapp.workbench.generator.ValidationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping(value="/api/validationCode")
@CrossOrigin(value="http://localhost:3000")
public class ValidationCodeController {

    private ValidationCode validationCode;
    private EmailService emailService;

    @Autowired
    public ValidationCodeController(ValidationCode validationCode, EmailService emailService){
        this.validationCode = validationCode;
        this.emailService = emailService;
    }

    @GetMapping("/generate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchGeneratedValidationCode(){
        String code = validationCode.generateValidationCode();
        return ResponseEntity.ok(code);
    }

    @PostMapping("/send-verification-email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody Map<String, String> requestBody){
        String email = requestBody.get("email");
        String code = requestBody.get("code");
        String subject = "Your Verification Code";

        // Setting up the context for the email template
        Context context = new Context();
        context.setVariable("verificationCode", code); // The verification code

        // Template name assumed to be configured for your project
        String templateName = "verificationEmailTemplate";

        CompletableFuture<Boolean> emailSent = emailService.sendEmail(email, "no-reply@yourdomain.com", templateName, context, subject);

        if (emailSent.join()) {
            return ResponseEntity.ok("Email sent successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
        }
    }
}

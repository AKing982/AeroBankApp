package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.workbench.tokens.AuthTokenResponse;
import com.example.aerobankapp.workbench.utilities.LoginRequest;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@Getter
public class AuthController
{

    private final AuthenticationServiceImpl authenticationService;
    private Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthenticationServiceImpl authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody LoginRequest loginRequest)
    {
        try
        {
            String token = authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok().body(new AuthTokenResponse(token));

        }catch(AuthenticationException ex)
        {
            LOGGER.debug("Authentication Exception for user: " + loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    @GetMapping("/status")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> checkAuthenticationStatus(@RequestBody Principal principal)
    {
        if(principal != null)
        {
            return ResponseEntity.ok().body("User is Authenticated");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getUserProfile(Authentication authentication)
    {
        return null;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request)
    {
        return "redirect:/login?logout";
    }
}

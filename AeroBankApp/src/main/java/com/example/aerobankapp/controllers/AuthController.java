package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.workbench.tokens.AuthTokenResponse;
import com.example.aerobankapp.workbench.utilities.LoginRequest;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import com.example.aerobankapp.workbench.utilities.response.AuthDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/auth")
@Getter
public class AuthController {

    private final AuthenticationServiceImpl authenticationService;
    private Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
        if (authenticationService == null) {
            LOGGER.error("Authentication Service is null");
        }
    }

    @PostMapping(value = "/login")
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public ResponseEntity<?> createAuthenticateToken(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername().trim();
        String password = loginRequest.getPassword().trim();

        try {
            AuthDataResponse authResponse = getAuthenticationToken(username, password);
            LOGGER.warn("Token: " + authResponse.getToken());
            if (authResponse.getToken() == null) {
                LOGGER.warn("Authentication Failed for User: " + loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token Generated");
            }
            LOGGER.debug("User Generated Token: " + authResponse.getToken());
            // return ResponseEntity.ok(new AuthTokenResponse(authToken, "Bearer"));
            return ResponseEntity.ok(authResponse);

        } catch (AuthenticationException ex) {
            LOGGER.debug("Authentication Exception for user: " + loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    private AuthDataResponse getAuthenticationToken(String user, String password) {
        LOGGER.warn("User: " + user);
        LOGGER.warn("Password: " + password);
        return getAuthenticationService().login(user, password);
    }

    @GetMapping("/status")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> checkAuthenticationStatus(@RequestBody Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok().body("User is Authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(false);
        if (session != null) {
            LOGGER.info("Invalidating Session: " + session.getId());
            session.invalidate();
        }else{
            LOGGER.info("No Session to invalidate");
        }
        return ResponseEntity.ok().body("User logged out successfully");
    }

}

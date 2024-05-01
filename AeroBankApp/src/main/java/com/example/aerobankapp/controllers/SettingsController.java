package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@CrossOrigin(origins = "http://localhost:3000")
public class SettingsController
{
    private UserService userService;
    private AccountService accountService;
    private AccountCodeService accountCodeService;
    private AccountSecurityService accountSecurityService;
    private AccountPropertiesService accountPropertiesService;
    private UserLogService userLogService;

    @PostMapping("/delete-user/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser(@PathVariable int userID){
        return null;
    }
}

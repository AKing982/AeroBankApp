package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.UserLogDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="api/session")
@CrossOrigin(value="http://localhost:3000")
public class UserLogController
{
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserLogById(@PathVariable int id)
    {
        return null;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllUserLogs()
    {
        return null;
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addUserLog(@Valid @RequestBody UserLogDTO userLogDTO)
    {
        return null;
    }
}

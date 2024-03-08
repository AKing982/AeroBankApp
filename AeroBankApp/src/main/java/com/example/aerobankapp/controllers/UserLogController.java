package com.example.aerobankapp.controllers;


import com.example.aerobankapp.dto.UserLogDTO;
import com.example.aerobankapp.services.UserLogService;
import com.example.aerobankapp.workbench.UserLogResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/api/session")
@CrossOrigin(value="http://localhost:3000")
public class UserLogController
{
    private final UserLogService userLogService;

    @Autowired
    public UserLogController(UserLogService userLogService)
    {
        this.userLogService = userLogService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<UserLogResponse> getUserLogById(@PathVariable int id)
    {
        return null;
       // return ResponseEntity.ok(new UserLogResponse(id, 1, "AKing94", LocalDateTime.now(), LocalDateTime.now(), 6049, true, 1, "ajajajsdfasdf"));
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

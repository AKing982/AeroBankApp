package com.example.aerobankapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/profile", method= RequestMethod.GET)
@CrossOrigin(value="http://localhost:3000")
public class UserProfileController {


    @GetMapping(value="/data")
    public ResponseEntity<?> getUserProfileData()
    {
        
    }
}

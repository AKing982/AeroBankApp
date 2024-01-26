package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.UserDAOImpl;
import com.example.aerobankapp.workbench.utilities.User;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/profile", method= RequestMethod.GET)
@CrossOrigin(value="http://localhost:3000")
public class UserProfileController {

    private UserDAOImpl userDAO;

    @Autowired
    public UserProfileController(UserDAOImpl userDAO)
    {
        this.userDAO = userDAO;
    }

    @GetMapping(value="/data/{username}")
    public ResponseEntity<?> getUserProfileData(@PathVariable String username)
    {
        return ResponseEntity.ok(null);
    }
}

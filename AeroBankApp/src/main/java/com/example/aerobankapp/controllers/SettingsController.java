package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.data.UserDataManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/settings")
@CrossOrigin(origins = "http://localhost:3000")
public class SettingsController
{
    private UserDataManagerImpl userDataManager;

    @Autowired
    public SettingsController(UserDataManagerImpl userDataManager){
        this.userDataManager = userDataManager;
    }

    @DeleteMapping("/delete-user/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser(@PathVariable int userID){

        boolean isDeleted = userDataManager.cascadeDeleteAllUserData(userID);
        return ResponseEntity.ok(isDeleted);
    }
}

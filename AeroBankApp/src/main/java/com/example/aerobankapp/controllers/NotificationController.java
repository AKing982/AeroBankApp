package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

@Controller
@RequestMapping(value="/api/notifications", method= RequestMethod.GET)
@CrossOrigin(origins="http://localhost:3000")
public class NotificationController
{
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }

    @GetMapping("/deposit/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getDepositNotifications(@PathVariable int userID)
    {
        return null;
    }

    @GetMapping("/withdraw/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWithdrawNotifications(@PathVariable int userID)
    {
        return null;
    }

    @GetMapping("/transfer/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTransferNotifications(@PathVariable int userID)
    {
        return null;
    }

    @GetMapping("/email")
    public ResponseEntity<?> sendEmailNotification()
    {
        return null;
    }

    @GetMapping("/message")
    public ResponseEntity<?> sendMessageNotification()
    {
        return null;
    }
}

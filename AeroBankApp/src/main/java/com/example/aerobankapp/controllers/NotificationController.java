package com.example.aerobankapp.controllers;

import com.example.aerobankapp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

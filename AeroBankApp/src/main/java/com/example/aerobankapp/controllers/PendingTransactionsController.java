package com.example.aerobankapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value="/pending", method=RequestMethod.GET)
@CrossOrigin(origins="http://localhost:3000")
public class PendingTransactionsController
{

    @GetMapping("/size")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> totalPendingTransctions()
    {
        return null;
    }
}

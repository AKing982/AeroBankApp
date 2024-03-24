package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transfers")
@CrossOrigin(value="http://localhost:3000")
public class TransferController
{
    private TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveTransfer(@RequestBody @Valid TransferDTO transferDTO)
    {
        return null;
    }
}

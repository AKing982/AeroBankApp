package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.engine.DepositEngine;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/deposits")
@CrossOrigin(value="http://localhost:3000")
public class DepositController {

    private DepositEngine depositEngine


    @GetMapping("/data/{accountID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DepositDTO>> getDeposits(@PathVariable Long accountID)
    {
        return null;
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createDeposit(@Valid @RequestBody DepositDTO depositDTO)
    {


        return ResponseEntity.ok("Posted");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id)
    {
        return null;
    }

}

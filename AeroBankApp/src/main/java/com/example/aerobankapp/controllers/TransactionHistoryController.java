package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.TransactionHistoryDTO;
import com.example.aerobankapp.model.TransactionHistory;
import com.example.aerobankapp.services.TransactionHistoryService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/transactionHistory")
@CrossOrigin(value = "http://localhost:3000")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    private final Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryController.class);

    @Autowired
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService){
        this.transactionHistoryService = transactionHistoryService;
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sendTransactionHistoryRequest(@RequestBody TransactionHistoryDTO transactionHistoryDTO){
        LOGGER.info("TransactionHistory request: " + transactionHistoryDTO);

        return ResponseEntity.ok("Successfully sent transaction history request");
    }

}

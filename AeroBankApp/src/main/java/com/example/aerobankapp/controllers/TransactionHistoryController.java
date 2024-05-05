package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.TransactionHistoryDTO;
import com.example.aerobankapp.model.TransactionHistory;
import com.example.aerobankapp.services.TransactionHistoryService;
import com.example.aerobankapp.workbench.transactionHistory.TransactionHistoryParser;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactionHistory.queries.TransactionHistoryQueryRunner;
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
    private TransactionHistoryParser transactionHistoryParser;
    private TransactionHistoryQueryRunner transactionHistoryQueryRunner;
    private final Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryController.class);

    @Autowired
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService, TransactionHistoryQueryRunner transactionHistoryQueryRunner){
        this.transactionHistoryService = transactionHistoryService;
        this.transactionHistoryQueryRunner = transactionHistoryQueryRunner;
    }

    @GetMapping("/data/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getDefaultUserHistoryData(@PathVariable int userID){
        List<?> queryList = transactionHistoryQueryRunner.runDefaultQueryWithUserID(userID);

        return ResponseEntity.ok(queryList);
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sendTransactionHistoryRequest(@RequestBody TransactionHistoryDTO transactionHistoryDTO){
        LOGGER.info("TransactionHistory request: " + transactionHistoryDTO);
        transactionHistoryParser = new TransactionHistoryParser(transactionHistoryDTO);
        HistoryCriteria historyCriteria = new HistoryCriteria(transactionHistoryDTO.userID(), transactionHistoryDTO.description(), transactionHistoryDTO.minAmount(), transactionHistoryDTO.maxAmount(), transactionHistoryDTO.startDate(), transactionHistoryDTO.endDate(), transactionHistoryDTO.scheduledTime(), null, transactionHistoryDTO.status(), transactionHistoryDTO.transactionType());

        List<?> queryList = transactionHistoryQueryRunner.runQuery(historyCriteria);

        return ResponseEntity.ok(queryList);
    }

}

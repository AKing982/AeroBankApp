package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.TransactionHistoryDTO;
import com.example.aerobankapp.model.TransactionHistory;
import com.example.aerobankapp.services.TransactionHistoryService;
import com.example.aerobankapp.workbench.transactionHistory.TransactionHistoryParser;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactionHistory.queries.TransactionHistoryQueryRunner;
import com.example.aerobankapp.workbench.utilities.response.TransactionStatsResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/stats/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTransactionStatistics(@PathVariable int userID){
        String totalTransactionAmountForMonth = transactionHistoryQueryRunner.runTotalAmountForMonthQuery(userID);
        String totalAverageTransactionValue = transactionHistoryQueryRunner.runAverageTransactionValueQuery(userID);
        String totalTransferredAmount = transactionHistoryQueryRunner.runTotalAmountTransferredQuery(userID);
        Long totalPending = transactionHistoryQueryRunner.runPendingTransactionCountQuery(userID);

        TransactionStatsResponse transactionStatsResponse = new TransactionStatsResponse(totalTransferredAmount, totalAverageTransactionValue, totalPending, totalTransactionAmountForMonth);
        return ResponseEntity.ok(transactionStatsResponse);
    }

    @GetMapping("/amount/month/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTotalTransactionAmountForMonth(@PathVariable int userID){
        String totalTransactionAmountForMonth = transactionHistoryQueryRunner.runTotalAmountForMonthQuery(userID);
        return ResponseEntity.ok(totalTransactionAmountForMonth);
    }

}

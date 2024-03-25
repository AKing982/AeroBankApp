package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.TransactionStatementEntity;
import com.example.aerobankapp.services.TransactionStatementService;
import com.example.aerobankapp.workbench.utilities.PendingAmountResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value="http://localhost:3000")
@RequestMapping(value="/api/transactionStatements", method=RequestMethod.GET)
public class TransactionStatementController
{
    private final TransactionStatementService transactionStatementService;

    @Autowired
    public TransactionStatementController(TransactionStatementService transactionStatementService)
    {
        this.transactionStatementService = transactionStatementService;
    }

    @GetMapping("/{acctID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTransactionStatementsByAcctID(@PathVariable @Valid int acctID)
    {
        List<TransactionStatementEntity> transactionStatementEntityList = transactionStatementService.getTransactionStatementsByAcctID(acctID);
        if(transactionStatementEntityList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionStatementEntityList);
    }

    @GetMapping("/pending/{acctID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPendingTransactions(@PathVariable int acctID){
        List<TransactionStatementEntity> pendingTransactions = transactionStatementService.getPendingTransactionsByAcctID(acctID);
        if(pendingTransactions.isEmpty()){
            return ResponseEntity.badRequest().body("Bad Request. Found no Pending Transactions.");
        }
        return ResponseEntity.ok().body(pendingTransactions);
    }

    @GetMapping("/pending/size/{acctID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> totalPendingTransactions(@PathVariable int acctID){
        int countOfPending = transactionStatementService.getCountOfPendingTransactionsByAcctID(acctID);
        String countOfStr = String.valueOf(countOfPending);

        return ResponseEntity.ok(new PendingAmountResponse(countOfStr));
    }

}

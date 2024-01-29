package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.utilities.response.DepositResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/deposits", method = RequestMethod.POST)
@CrossOrigin(value="http://localhost:3000")
public class DepositController {


    @GetMapping("/data/{accountID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DepositDTO>> getDeposits(@PathVariable Long accountID)
    {
        return null;
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin(origins="http://localhost:3000")
    @ResponseBody
    public ResponseEntity<?> createDeposit(@Valid @RequestBody DepositDTO depositDTO)
    {
        DepositResponse depositResponse = getDepositResponse(depositDTO);
        System.out.println("AccountCode: " + depositDTO.accountCode());
        System.out.println("Deposit Amount: " + depositDTO.amount());
        System.out.println("Description: " + depositDTO.description());
        System.out.println("Scheduled Interval: " + depositDTO.scheduleInterval());
        System.out.println("Scheduled Time: " + depositDTO.timeScheduled());
        System.out.println("Scheduled Date: " + depositDTO.date());
        return ResponseEntity.ok(depositResponse);
    }

    private DepositResponse getDepositResponse(final DepositDTO depositDTO)
    {
        return DepositResponse.builder()
                .amount(depositDTO.amount())
                .description(depositDTO.description())
                .accountCode(depositDTO.accountCode())
                .interval(depositDTO.scheduleInterval())
                .scheduledDate(depositDTO.date())
                .selectedTime(depositDTO.timeScheduled())
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id)
    {
        return null;
    }

}

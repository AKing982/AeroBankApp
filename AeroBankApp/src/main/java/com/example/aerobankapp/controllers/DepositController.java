package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.utilities.DepositRequest;
import com.example.aerobankapp.workbench.utilities.response.DepositResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.aerobankapp.controllers.utils.DepositControllerUtil.getDepositResponse;

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
    public ResponseEntity<?> createDeposit(@Valid @RequestBody DepositRequest request)
    {
        DepositResponse depositResponse = getDepositResponse(request);
        System.out.println("AccountCode: " + request.getAccountCode());
        System.out.println("Deposit Amount: " + request.getAmount());
        System.out.println("Description: " + request.getDescription());
        System.out.println("Scheduled Interval: " + request.getScheduleInterval());
        System.out.println("Scheduled Time: " + request.getTimeScheduled());
        System.out.println("Scheduled Date: " + request.getDate());
        return ResponseEntity.ok(depositResponse);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteDeposit(@PathVariable Long id)
    {
        return null;
    }

}

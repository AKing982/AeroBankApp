package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.services.DepositService;
import com.example.aerobankapp.workbench.utilities.DepositRequest;
import com.example.aerobankapp.workbench.utilities.response.DepositResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.aerobankapp.controllers.utils.DepositControllerUtil.*;

@RestController
@RequestMapping(value="/api/deposits")
@CrossOrigin(value="http://localhost:3000")
public class DepositController {

    private final DepositService depositService;
    private final DepositEngine depositEngine;

    @Autowired
    public DepositController(@Qualifier("depositServiceImpl") DepositService depositService, DepositEngine depositEngine)
    {
        this.depositService = depositService;
        this.depositEngine = depositEngine;
    }

    @GetMapping("/data/{accountID}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<List<DepositResponse>> getDeposits(@PathVariable int accountID)
    {
       List<DepositsEntity> depositsEntities = depositService.getDepositsByAcctID(accountID);
       List<DepositResponse> depositDTOS = getDepositResponseList(depositsEntities);

       return ResponseEntity.ok(depositDTOS);
    }

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<?> submitDeposit(@Valid @RequestBody DepositRequest request)
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

    @PostMapping("/{id}/confirm")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> confirmDeposit(@PathVariable Long id)
    {
        return null;
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DepositDTO>> getAllDepositsByUser(@PathVariable Long id)
    {
        return null;
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DepositDTO>> search(@RequestParam Map<String, String> criteria)
    {
        return null;
    }

    @GetMapping("/scheduled")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DepositDTO>> getScheduledDeposits()
    {
        return null;
    }


    @PostMapping("/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelScheduledDeposit(@PathVariable Long id)
    {
        return null;
    }

}

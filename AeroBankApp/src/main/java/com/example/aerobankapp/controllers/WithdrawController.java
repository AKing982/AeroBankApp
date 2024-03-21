package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.WithdrawDTO;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.services.WithdrawService;
import com.example.aerobankapp.workbench.utilities.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value="/api/withdraw")
@CrossOrigin(value="http://localhost:3000")
public class WithdrawController {

    private final WithdrawService withdrawService;

    private Logger LOGGER = LoggerFactory.getLogger(WithdrawController.class);

    @Autowired
    public WithdrawController(WithdrawService withdrawService){
        this.withdrawService = withdrawService;
    }

    @GetMapping(value="/user/{userID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWithdrawalsByUserID(@PathVariable int userID)
    {
        List<WithdrawEntity> withdrawEntityList = withdrawService.findByUserID(userID);
        return ResponseEntity.ok(withdrawEntityList);
    }

    @GetMapping("/account/{acctID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWithdrawalsByAcctID(@PathVariable int acctID){
        if(acctID < 1){
            return ResponseEntity.badRequest().body("Unable to retrieve Withdrawal Entities for acctID: " + acctID);
        }
        List<WithdrawEntity> withdrawEntities = withdrawService.findByAccountID(acctID);
        return ResponseEntity.ok(withdrawEntities);
    }

    @GetMapping("/between")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findWithdrawalsBetweenDates(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                         @RequestParam("endDate") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate endDate){
        List<WithdrawEntity> withdrawEntities = withdrawService.findWithdrawBetweenDates(startDate, endDate);
        return ResponseEntity.ok(withdrawEntities);
    }

    @GetMapping("/{userID}/ascending")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findWithdrawalsByUserIDAscending(@PathVariable int userID)
    {
        if(userID < 1){
            return ResponseEntity.badRequest().body("Unable to retrieve withdrawals due to invalid UserID: " + userID);
        }
        List<WithdrawEntity> withdrawEntities = withdrawService.getListOfWithdrawalsByUserIDAsc(userID);

        return ResponseEntity.ok(withdrawEntities);
    }

    @GetMapping("/{userID}/descending")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> findWithdrawalsByUserIDDescending(@PathVariable int userID){
        if(userID < 1){
            return ResponseEntity.badRequest().body("Unable to retrieve withdrawals due to invalid UserID: " + userID);
        }

        List<WithdrawEntity> withdrawEntities = withdrawService.getListOfWithdrawalsByUserIDDesc(userID);
        return ResponseEntity.ok(withdrawEntities);
    }

    @GetMapping("/name/{user}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWithdrawalsByUserName(@PathVariable String user){
        if(user.isEmpty()){
            return ResponseEntity.notFound().build();
        }


        List<WithdrawEntity> withdrawEntities = withdrawService.findByUserName(user);
        return ResponseEntity.ok(withdrawEntities);
    }

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWithdrawalsByStatus(@RequestParam(value="status", required = false) Status status){
        if(status == null){
            return ResponseEntity.badRequest().body("Unable to fetch Withdrawals due to null status.");
        }

        List<WithdrawEntity> withdrawEntities = withdrawService.findByStatus(status);

        return ResponseEntity.ok().body(withdrawEntities);
    }

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addWithdrawal(@RequestBody WithdrawDTO withdrawDTO){
        if(withdrawDTO == null){
            return ResponseEntity.status(403).body("Withdraw request is forbidden.");
        }

        return null;
    }

    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteWithdrawal(@RequestBody WithdrawDTO withdrawDTO){
        return null;
    }


}

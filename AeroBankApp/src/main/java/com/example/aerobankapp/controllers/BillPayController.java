package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.BillPaymentDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import com.example.aerobankapp.dto.ScheduledPaymentDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.billPayment.BillPaymentQueries;
import com.example.aerobankapp.workbench.formatter.FormatterUtil;
import com.example.aerobankapp.workbench.utilities.response.BillPayeesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.aerobankapp.services.utilities.BillPayeesServiceUtil.getBillPayeeResponseList;

@Controller
@RequestMapping(value="/api/bills")
public class BillPayController {

    private BillPaymentService billPaymentService;

    private BillPaymentScheduleService billPaymentScheduleService;

    private BillPaymentHistoryService billPaymentHistoryService;

    private BillPaymentQueries billPaymentQueries;

    private Logger LOGGER = LoggerFactory.getLogger(BillPayController.class);

    @Autowired
    public BillPayController(BillPaymentService billPaymentService,
                             BillPaymentScheduleService billPaymentScheduleService,
                             BillPaymentHistoryService billPaymentHistoryService,
                             BillPaymentQueries billPaymentQueries){
        this.billPaymentService = billPaymentService;
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentHistoryService = billPaymentHistoryService;
        this.billPaymentQueries = billPaymentQueries;
    }

    @GetMapping("/{userID}/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchBillPayeesList(@PathVariable int userID){
        if(userID < 1){
            return ResponseEntity.badRequest().body("Invalid UserID found: " + userID);
        }
        List<BillPaymentEntity> billPaymentEntities = billPaymentService.findBillPaymentsByUserID(userID);
        List<BillPayeesResponse> billPayeesResponses = getBillPayeeResponseList(billPaymentEntities);
        return ResponseEntity.ok(billPayeesResponses);
    }

    @GetMapping("/{userID}/schedules")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchUserBillSchedules(@PathVariable int userID){
        if(userID < 1){
            return ResponseEntity.badRequest().body("Invalid UserID found: " + userID);
        }
        List<BillPayeeInfoDTO> billPayeeInfoDTOS = billPaymentQueries.getBillPaymentScheduleQuery(userID);
        List<ScheduledPaymentDTO> scheduledPaymentDTOS = FormatterUtil.getFormattedBillPaymentSchedule(billPayeeInfoDTOS);
        return ResponseEntity.ok(scheduledPaymentDTOS);
    }

    @GetMapping("/{userID}/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchUserBillPaymentHistory(@PathVariable int userID){
        if(userID < 1){
            return ResponseEntity.badRequest().body("Invalid UserID found: " + userID);
        }
        List<PaymentHistoryDTO> paymentHistoryDTOS = billPaymentQueries.getBillPaymentHistoryQuery(userID);
        return ResponseEntity.ok(paymentHistoryDTOS);
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> savePaymentDetails(@RequestBody BillPaymentDTO billPaymentDTO){
        LOGGER.info("Bill Payment Criteria: " + billPaymentDTO.toString());

        // Create a Bill Payment Entity

        // Persist the Bill Payment Entity

        // Create a Bill Payment Schedule Entity

        // Persist the Bill Payment Schedule Entity

        // Create the Bill Payment History Entity

        // Persist the Bill Payment History entity

        return null;
    }

    private BillPaymentHistoryEntity createBillPaymentHistory(BillPaymentDTO billPaymentDTO){
        return BillPaymentHistoryEntity.builder()
                .build();
    }

    private BillPaymentScheduleEntity createBillPaymentSchedule(BillPaymentDTO billPaymentDTO){
        return BillPaymentScheduleEntity.builder()
                .build();
    }

    private BillPaymentEntity createBillPayment(BillPaymentDTO billPaymentDTO){
        return BillPaymentEntity.builder()
                .paymentAmount(billPaymentDTO.amount())
                .paymentType("ACCOUNT")
                .payeeName(billPaymentDTO.payeeName())
                .user(UserEntity.builder().userID(billPaymentDTO.userID()).build())
                .account(AccountEntity.builder().acctID(billPaymentDTO.acctID()).build())
                .postedDate(LocalDate.now())
                .build();

    }

}

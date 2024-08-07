package com.example.aerobankapp.controllers;

import com.example.aerobankapp.MyWebSocketHandler;
import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.BillPaymentDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import com.example.aerobankapp.dto.ScheduledPaymentDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentNotificationService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.billPayment.BillPaymentQueries;
import com.example.aerobankapp.workbench.formatter.FormatterUtil;
import com.example.aerobankapp.workbench.utilities.response.BillPayeesResponse;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
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
import static com.example.aerobankapp.services.utilities.BillPaymentHistoryServiceUtils.createBillPaymentHistory;
import static com.example.aerobankapp.services.utilities.BillPaymentScheduleServiceUtils.createBillPaymentSchedule;
import static com.example.aerobankapp.services.utilities.BillPaymentServiceUtils.createBillPayment;

@Controller
@RequestMapping(value="/api/bills")
public class BillPayController {

    private BillPaymentService billPaymentService;

    private BillPaymentScheduleService billPaymentScheduleService;

    private BillPaymentHistoryService billPaymentHistoryService;

    private BillPaymentNotificationService billPaymentNotificationService;

    private BillPaymentQueries billPaymentQueries;

    private MyWebSocketHandler myWebSocketHandler;

    private Logger LOGGER = LoggerFactory.getLogger(BillPayController.class);

    @Autowired
    public BillPayController(BillPaymentService billPaymentService,
                             BillPaymentScheduleService billPaymentScheduleService,
                             BillPaymentHistoryService billPaymentHistoryService,
                             BillPaymentNotificationService billPaymentNotificationService,
                             BillPaymentQueries billPaymentQueries){
        this.billPaymentService = billPaymentService;
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentHistoryService = billPaymentHistoryService;
        this.billPaymentNotificationService = billPaymentNotificationService;
        this.billPaymentQueries = billPaymentQueries;
        this.myWebSocketHandler = new MyWebSocketHandler();
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
        myWebSocketHandler.broadcastUpdatedData(scheduledPaymentDTOS);
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

        BillPaymentHistoryEntity billPaymentHistory = createBillPaymentHistory(billPaymentDTO);

        billPaymentHistoryService.save(billPaymentHistory);

        // Create a Bill Payment Schedule Entity
        BillPaymentScheduleEntity billPaymentSchedule = createBillPaymentSchedule(billPaymentDTO, billPaymentHistory);

        // Persist the Bill Payment Schedule Entity
        billPaymentScheduleService.save(billPaymentSchedule);

        // Create a Bill Payment Entity
        BillPaymentEntity billPayment = createBillPayment(billPaymentDTO, billPaymentSchedule);

        // Persist the Bill Payment Entity
        billPaymentService.save(billPayment);

        return ResponseEntity.ok("Payment was successfully saved");

    }

}
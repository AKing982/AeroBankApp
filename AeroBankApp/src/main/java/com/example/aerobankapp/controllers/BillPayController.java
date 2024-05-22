package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.BillPaymentDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import com.example.aerobankapp.dto.ScheduledPaymentDTO;
import com.example.aerobankapp.entity.BillPayeesEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.services.BillPayeesService;
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

import java.util.List;

import static com.example.aerobankapp.services.utilities.BillPayeesServiceUtil.getBillPayeeResponseList;

@Controller
@RequestMapping(value="/api/bills")
public class BillPayController {

    private BillPayeesService billPayeesService;

    private BillPaymentService billPaymentService;

    private BillPaymentQueries billPaymentQueries;

    private Logger LOGGER = LoggerFactory.getLogger(BillPayController.class);

    @Autowired
    public BillPayController(BillPayeesService billPayeesService,
                             BillPaymentService billPaymentService,
                             BillPaymentQueries billPaymentQueries){
        this.billPayeesService = billPayeesService;
        this.billPaymentService = billPaymentService;
        this.billPaymentQueries = billPaymentQueries;
    }

    @GetMapping("/{userID}/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchBillPayeesList(@PathVariable int userID){
        List<BillPayeesEntity> billPayeesEntityList = billPayeesService.findBillPayeesByUserID(userID);
        List<BillPayeesResponse> billPayeesResponses = getBillPayeeResponseList(billPayeesEntityList);
        return ResponseEntity.ok(billPayeesResponses);
    }

    @GetMapping("/{userID}/schedules")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchUserBillSchedules(@PathVariable int userID){
        List<BillPayeeInfoDTO> billPayeeInfoDTOS = billPaymentQueries.getBillPaymentScheduleQuery(userID);
        List<ScheduledPaymentDTO> scheduledPaymentDTOS = FormatterUtil.getFormattedBillPaymentSchedule(billPayeeInfoDTOS);
        return ResponseEntity.ok(scheduledPaymentDTOS);
    }

    @GetMapping("/{userID}/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> fetchUserBillPaymentHistory(@PathVariable int userID){
        List<PaymentHistoryDTO> paymentHistoryDTOS = billPaymentQueries.getBillPaymentHistoryQuery(userID);
        return ResponseEntity.ok(paymentHistoryDTOS);
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> savePaymentDetails(@RequestBody BillPaymentDTO billPaymentDTO){
        LOGGER.info("Bill Payment Criteria: " + billPaymentDTO.toString());
        return null;
    }

}

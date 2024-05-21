package com.example.aerobankapp.controllers;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.entity.BillPayeesEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.services.BillPayeesService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.workbench.utilities.response.BillPayeesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.example.aerobankapp.services.utilities.BillPayeesServiceUtil.getBillPayeeResponseList;

@Controller
@RequestMapping(value="/api/bills")
public class BillPayController {

    private BillPayeesService billPayeesService;

    private BillPaymentService billPaymentService;


    public BillPayController(BillPayeesService billPayeesService,
                             BillPaymentService billPaymentService){
        this.billPayeesService = billPayeesService;
        this.billPaymentService = billPaymentService;
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
        List<BillPayeeInfoDTO> billPayeeInfoDTOS = billPaymentService.findBillPaymentScheduleInfoByUserID(userID);
        return ResponseEntity.ok(billPayeeInfoDTOS);
    }
}

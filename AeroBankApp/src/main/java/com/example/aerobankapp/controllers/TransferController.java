package com.example.aerobankapp.controllers;

import com.example.aerobankapp.converter.TransferConverter;
import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import com.example.aerobankapp.workbench.utilities.conversion.TransferMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/api/transfers")
@CrossOrigin(value="http://localhost:3000")
public class TransferController
{
    private TransferService transferService;
    private TransferMapper transferMapper;

    private Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    public TransferController(TransferService transferService,
                              TransferMapper transferMapper){
        this.transferService = transferService;
        this.transferMapper = transferMapper;
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveTransfer(@RequestBody @Valid TransferDTO transferDTO) {
        if (transferDTO == null){
            return ResponseEntity.badRequest().body("TransferDTO is null");
        }

        LOGGER.info("Transfer DTO type: " + transferDTO.transferType());
        TransferEntity transfer = transferMapper.fromDTO(transferDTO);

        transferService.save(transfer);

       return ResponseEntity.ok("Transfer Request submitted successfully.");
    }

}

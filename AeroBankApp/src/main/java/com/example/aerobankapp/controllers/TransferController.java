package com.example.aerobankapp.controllers;

import com.example.aerobankapp.converter.TransferConverter;
import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.TransferService;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
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
    private AccountService accountService;
    private UserService userService;

    private Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    public TransferController(TransferService transferService,
                              AccountService accountService,
                              UserService userService){
        this.transferService = transferService;
        this.accountService = accountService;
        this.userService = userService;
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> saveTransfer(@RequestBody @Valid TransferDTO transferDTO)
    {
        LOGGER.info("Transfer DTO type: " + transferDTO.transferType());
        TransferEntity transfer = buildTransferEntity(transferDTO);

        transferService.save(transfer);

        return ResponseEntity.ok("Transfer Request submitted successfully.");
    }

    private TransferEntity buildTransferEntity(TransferDTO transferDTO){
        TransferEntity transfer = new TransferEntity();
        transfer.setTransferID(transferDTO.transferID()); // Make sure getters are properly named
        transfer.setDescription(transferDTO.transferDescription());
        transfer.setTransferAmount(transferDTO.transferAmount());
        transfer.setTransferDate(transferDTO.transferDate());
        transfer.setTransferTime(transferDTO.transferTime());

        // Fetch and set the user entities
        UserEntity toUser = userService.findById(transferDTO.toUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid toUserID: " + transferDTO.toUserID()));
        UserEntity fromUser = userService.findById(transferDTO.fromUserID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid fromUserID: " + transferDTO.fromUserID()));
        transfer.setToUser(toUser);
        transfer.setFromUser(fromUser);

        // Fetch and set the account entities
        AccountEntity toAccount = accountService.findById(transferDTO.toAccountID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid toAccountID: " + transferDTO.toAccountID()));
        AccountEntity fromAccount = accountService.findById(transferDTO.fromAccountID())
                .orElseThrow(() -> new IllegalArgumentException("Invalid fromAccountID: " + transferDTO.fromAccountID()));

        transfer.setToAccount(toAccount);
        transfer.setFromAccount(fromAccount);

        transfer.setNotificationEnabled(transferDTO.notificationEnabled());
        transfer.setStatus(TransferStatus.PENDING);
        transfer.setTransferType(transferDTO.transferType());
        LOGGER.info("Transfer Type: " + transfer.getTransferType());

        return transfer;
    }
}

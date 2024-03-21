package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.workbench.utilities.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferDTO(Long transferID,
                          User user,
                          Account fromAccount,
                          Account toAccount,
                          BigDecimal amount,
                          String description,
                          boolean isUserToUserTransfer,
                          LocalDate dateTransferred)
{

}

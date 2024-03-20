package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.model.Account;

public record TransferDTO(Long transferID,
                          Account fromAccount,
                          Account toAccount)
{

}

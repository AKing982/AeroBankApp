package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AccountPrefix;
import com.example.aerobankapp.account.AccountType;

public record AccountNumberDTO(AccountPrefix accountPrefix,
                               String branchIdentifier,
                               String accountNum,
                               AccountType accountType)
{

}

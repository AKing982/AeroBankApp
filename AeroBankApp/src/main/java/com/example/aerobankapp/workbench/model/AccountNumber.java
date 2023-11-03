package com.example.aerobankapp.workbench.model;

import com.example.aerobankapp.account.AccountType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AccountNumber
{
    private int firstSeg;
    private int secondSeg;
    private int lastSeg;
    private UserType
    private AccountType accountType;
}

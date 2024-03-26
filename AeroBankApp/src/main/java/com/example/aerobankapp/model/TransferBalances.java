package com.example.aerobankapp.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferBalances
{
    private BigDecimal toAccountBalance;
    private BigDecimal fromAccountBalance;

    public TransferBalances(BigDecimal toAccountBal, BigDecimal fromAccountBal){
        this.toAccountBalance = toAccountBal;
        this.fromAccountBalance = fromAccountBal;
    }

    public TransferBalances(){

    }
}

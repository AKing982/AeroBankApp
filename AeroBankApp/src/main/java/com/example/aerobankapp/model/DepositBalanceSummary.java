package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.Deposit;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DepositBalanceSummary
{
    private Deposit deposit;
    private BigDecimal balanceAfterDeposit;
    private LocalDate dateProcessed;

    public DepositBalanceSummary(Deposit deposit, BigDecimal balanceAfterDeposit, LocalDate dateProcessed) {
        this.deposit = deposit;
        this.balanceAfterDeposit = balanceAfterDeposit;
        this.dateProcessed = dateProcessed;
    }

    public DepositBalanceSummary(){
        // Nothing here to see
    }

}

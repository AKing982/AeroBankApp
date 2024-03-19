package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.transactions.Deposit;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DepositBalanceSummary extends TransactionBalanceSummary<Deposit>
{
    public DepositBalanceSummary(Deposit deposit, BigDecimal balanceAfterDeposit, LocalDate dateProcessed) {
      super(deposit, balanceAfterDeposit, dateProcessed);

    }

    public DepositBalanceSummary(){
        super();
    }

    @Override
    protected String generateReportSummary() {
        return null;
    }

}

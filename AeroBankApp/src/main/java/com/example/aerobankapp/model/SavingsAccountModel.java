package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AbstractAccountBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccountModel extends AbstractAccountBase
{
    private String id;
    private final BigDecimal interestRate = new BigDecimal("0.00");
    private final BigDecimal minBalance = new BigDecimal("500.00");
    private final BigDecimal dividend = new BigDecimal("0.78");
    private final int maxWithdrawals = 2;

    @Override
    protected void deposit(BigDecimal amount)
    {

    }

    @Override
    protected void withdraw(BigDecimal amount)
    {

    }

    @Override
    protected BigDecimal getBalance()
    {
        return null;
    }
}

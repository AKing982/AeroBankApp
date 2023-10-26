package com.example.aerobankapp.account;

import com.example.aerobankapp.fees.FeesDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Component
public abstract class AbstractAccountBase
{
    protected String accountName;
    protected int userID;
    protected BigDecimal interestRate;
    protected BigDecimal minimumBalance;
    protected BigDecimal balance;
    protected FeesDTO accountFees;
    protected int maxWithdrawals;
    protected int maxDeposits;

    public AbstractAccountBase(String name, BigDecimal interestRate, BigDecimal minimumBalance, FeesDTO accountFees, int maxWithdrawals, int maxDeposits) {
        this.accountName = name;
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
        this.accountFees = accountFees;
        this.maxWithdrawals = maxWithdrawals;
        this.maxDeposits = maxDeposits;
    }

    public AbstractAccountBase(BigDecimal interestRate, BigDecimal minimumBalance)
    {
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    protected abstract void deposit(BigDecimal amount);

    protected abstract void withdraw(BigDecimal amount);

    protected abstract BigDecimal getBalance();
}

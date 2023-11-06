package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CheckingAccount extends AbstractAccountBase
{
    private String id;
    private final AccountType accountType = AccountType.CHECKING;
    private BigDecimal interestRate = new BigDecimal("2.56");
    private BigDecimal minBalance = new BigDecimal("150.00");

    public CheckingAccount(BigDecimal interest, BigDecimal min_balance)
    {
        super(interest, min_balance);
    }

    @Override
    public void deposit(BigDecimal amount)
    {
        super.balance.add(amount);
    }

    @Override
    public void withdraw(BigDecimal amount)
    {
        super.balance.subtract(amount);
    }

    @Override
    protected BigDecimal getBalance()
    {
        return balance;
    }
}

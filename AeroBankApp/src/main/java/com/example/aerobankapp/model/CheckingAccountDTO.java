package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.account.AccountDetails;
import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.fees.FeesDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Builder
public class CheckingAccountDTO extends AbstractAccountBase
{
    private String id;
    private final AccountType accountType = AccountType.CHECKING;

    @Builder.Default
    private BigDecimal interestRate = BigDecimal.ZERO;

    public CheckingAccountDTO(String accountName, UserDTO user, BigDecimal balance, BigDecimal interestRate, FeesDTO accountFees, BigDecimal minimumBalance, int numberOfWithdrawals, int maxDeposits) {
        super(accountName, interestRate, minimumBalance, accountFees, numberOfWithdrawals, maxDeposits);
        this.user = user;
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountFees = accountFees;
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void deposit(BigDecimal amount)
    {

    }

    @Override
    public void withdraw(BigDecimal amount)
    {

    }
}

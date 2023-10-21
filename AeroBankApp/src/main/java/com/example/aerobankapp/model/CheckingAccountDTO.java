package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.account.AccountDetails;
import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.fees.FeesDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CheckingAccountDTO extends AbstractAccountBase
{
    private String id;
    private final AccountType accountType = AccountType.CHECKING;

    private BigDecimal interestRate = BigDecimal.ZERO;


    @Override
    public void deposit(BigDecimal amount)
    {

    }

    @Override
    public void withdraw(BigDecimal amount)
    {

    }
}

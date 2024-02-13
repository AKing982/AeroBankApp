package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;



@NoArgsConstructor
@Component
public class Deposit extends TransactionBase
{
    private Long depositID;

    public Deposit(int userID, String descr, String acctID, BigDecimal amount, LocalDate date, TransactionStatus status)
    {
        super(userID, descr, acctID, amount, date, status);
    }

}

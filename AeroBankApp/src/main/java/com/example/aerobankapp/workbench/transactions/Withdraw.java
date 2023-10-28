package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Withdraw extends TransactionBase implements Serializable
{
    private Long id;
    private String toAccountID;
    private String fromAccountID;
    private AbstractAccountBase toAccount;
    private AbstractAccountBase fromAccount;

    public Withdraw(String descr, String toAccountID, String fromAccountID, BigDecimal amount, LocalDate date,
                    boolean isDebit, AbstractAccountBase toAccount, AbstractAccountBase fromAccount)
    {
        super(descr, amount, date, isDebit);
        this.toAccountID = toAccountID;
        this.fromAccountID = fromAccountID;
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
    }



}

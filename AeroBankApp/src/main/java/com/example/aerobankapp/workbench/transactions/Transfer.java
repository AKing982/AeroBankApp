package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transfer<T extends AbstractAccountBase> extends TransactionBase implements Serializable
{
    private Long transferID;
    private String toAcctID;
    private String fromAcctID;
    private T fromAccount;
    private T toAccount;

    public Transfer(int userID, String descr, BigDecimal amount, LocalDate date, TransactionStatus status,
                    String fromAccountID, String toAccountID,
                    T fromAccount, T toAccount)
    {
        super(userID, descr, fromAccountID, amount, date, status);
        this.toAcctID = toAccountID;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }


    @Override
    public void executeTransaction() {

    }

    @Override
    public boolean validateTransaction() {
        return false;
    }
}

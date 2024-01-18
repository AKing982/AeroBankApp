package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class Withdraw extends TransactionBase implements Serializable
{
    private Long id;
    private String fromAccountID;
    private AbstractAccountBase fromAccount;

    public Withdraw(int userID, String descr, String fromAccountID, BigDecimal amount, LocalDate date,
                    TransactionStatus status, AbstractAccountBase fromAccount)
    {
        super(userID, descr, fromAccountID, amount, date, status);
        this.fromAccountID = fromAccountID;
        this.fromAccount = fromAccount;
    }

    @Override
    public void executeTransaction() {

    }

    @Override
    public boolean validateTransaction() {
        return false;
    }
}

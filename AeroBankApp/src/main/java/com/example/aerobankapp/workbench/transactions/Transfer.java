package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class Transfer<T extends AbstractAccountBase> extends TransactionBase implements Serializable
{
    private Long transferID;
    private String toAcctID;
    private String fromAcctID;
    private T fromAccount;
    private T toAccount;

    public Transfer(Long transferID, String toAcctID, String fromAcctID, T fromAccount, T toAccount) {
        this.transferID = transferID;
        this.toAcctID = toAcctID;
        this.fromAcctID = fromAcctID;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public Transfer(int userID, String description, int accountID, BigDecimal amount, LocalDateTime timeScheduled, ScheduleType scheduleInterval, LocalDate date_posted, Currency currency, Long transferID, String toAcctID, String fromAcctID, T fromAccount, T toAccount) {
        super(userID, description, accountID, amount, timeScheduled, scheduleInterval, date_posted, currency);
        this.transferID = transferID;
        this.toAcctID = toAcctID;
        this.fromAcctID = fromAcctID;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }
}

package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Currency;


@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
@Getter
@Setter
public class Deposit extends TransactionBase implements Serializable
{
    private int depositID;
    private String acctCode;
    private int accountID;
    public Deposit(int depositID) {
        this.depositID = depositID;
    }

    public Deposit(int userID,
                   String description,
                   String acctCode,
                   int accountID,
                   BigDecimal amount,
                   LocalTime timeScheduled,
                   ScheduleType scheduleInterval,
                   LocalDate date_posted,
                   LocalDate dateScheduled,
                   Currency currency,
                   int depositID) {
        super(userID, description, amount, timeScheduled, scheduleInterval, date_posted, dateScheduled, currency);
        this.depositID = depositID;
        this.acctCode = acctCode;
        this.accountID = accountID;
    }
}

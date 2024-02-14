package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.scheduler.ScheduleType;
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
import java.time.LocalDateTime;
import java.util.Currency;


@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class Deposit extends TransactionBase implements Serializable
{
    private Long depositID;
    private String acctCode;

    public Deposit(Long depositID) {
        this.depositID = depositID;
    }

    public Deposit(int userID, String description, String acctCode, int accountID, BigDecimal amount, LocalDateTime timeScheduled, ScheduleType scheduleInterval, LocalDate date_posted, Currency currency, Long depositID) {
        super(userID, description, accountID, amount, timeScheduled, scheduleInterval, date_posted, currency);
        this.depositID = depositID;
        this.acctCode = acctCode;
    }
}

package com.example.aerobankapp.workbench.transactions;


import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Currency;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Builder
@Component
public class Withdraw extends TransactionBase implements Serializable
{
    private Long id;
    private int fromAccountID;

    public Withdraw(Long id, int fromAccountID) {
        this.id = id;
        this.fromAccountID = fromAccountID;
    }

    public Withdraw(int userID, String description, int accountID, BigDecimal amount, LocalTime timeScheduled, ScheduleType scheduleInterval, LocalDate date_posted, LocalDate dateScheduled, Currency currency, Long id, int fromAccountID) {
        super(userID, description, accountID, amount, timeScheduled, scheduleInterval, date_posted, dateScheduled, currency);
        this.id = id;
        this.fromAccountID = fromAccountID;
    }
}

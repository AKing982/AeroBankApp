package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.account.AbstractAccountBase;
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

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@Component
public class Withdraw extends TransactionBase implements Serializable
{
    private Long id;
    private int fromAccountID;
    private AbstractAccountBase fromAccount;

    public Withdraw(Long id, int fromAccountID, AbstractAccountBase fromAccount) {
        this.id = id;
        this.fromAccountID = fromAccountID;
        this.fromAccount = fromAccount;
    }

    public Withdraw(int userID, String description, int accountID, BigDecimal amount, LocalTime timeScheduled, ScheduleType scheduleInterval, LocalDate date_posted, LocalDate dateScheduled, Currency currency, Long id, int fromAccountID, AbstractAccountBase fromAccount) {
        super(userID, description, accountID, amount, timeScheduled, scheduleInterval, date_posted, dateScheduled, currency);
        this.id = id;
        this.fromAccountID = fromAccountID;
        this.fromAccount = fromAccount;
    }
}

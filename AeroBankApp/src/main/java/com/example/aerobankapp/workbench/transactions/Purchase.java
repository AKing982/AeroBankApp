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

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Component
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
public class Purchase extends TransactionBase implements Serializable
{
    private Long purchaseID;
    private String merchant;
    private int accountID;
    private PurchaseCategory category;

    public Purchase(Long purchaseID, String merchant, PurchaseCategory category) {
        this.purchaseID = purchaseID;
        this.merchant = merchant;
        this.category = category;
    }

    public Purchase(int userID, String description, int accountID, BigDecimal amount, LocalTime timeScheduled, ScheduleType scheduleInterval, LocalDate date_posted, LocalDate dateScheduled, Currency currency, Long purchaseID, String merchant, PurchaseCategory category) {
        super(userID, description, amount, timeScheduled, scheduleInterval, date_posted, dateScheduled, currency);
        this.purchaseID = purchaseID;
        this.merchant = merchant;
        this.category = category;
        this.accountID = accountID;
    }
}

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
    private PurchaseCategory category;

    public Purchase(Long purchaseID, String merchant, PurchaseCategory category) {
        this.purchaseID = purchaseID;
        this.merchant = merchant;
        this.category = category;
    }

    public Purchase(int userID, String description, int accountID, BigDecimal amount, LocalDateTime timeScheduled, ScheduleType scheduleInterval, LocalDate date_posted, Currency currency, Long purchaseID, String merchant, PurchaseCategory category) {
        super(userID, description, accountID, amount, timeScheduled, scheduleInterval, date_posted, currency);
        this.purchaseID = purchaseID;
        this.merchant = merchant;
        this.category = category;
    }
}

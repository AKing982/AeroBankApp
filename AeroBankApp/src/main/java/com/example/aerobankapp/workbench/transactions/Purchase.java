package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public Purchase(int userID, String merchant, PurchaseCategory purchaseCategory, String description, String acctID, BigDecimal amount, LocalDate timestamp, TransactionStatus status)
    {
        super(userID, description, acctID, amount, timestamp, status);
        this.merchant = merchant;
        this.category = purchaseCategory;
    }

}

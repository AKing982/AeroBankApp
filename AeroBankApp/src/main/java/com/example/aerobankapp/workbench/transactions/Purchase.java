package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Builder
@Component
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
public class Purchase extends TransactionBase implements Serializable
{
    private Long id;
    private String accountID;
    private CardDesignator card;

    public Purchase(Long id, String acctID, CardDesignator card)
    {
        super();
        this.id = id;
        this.accountID = acctID;
        this.card = card;
    }

    public Purchase(String descr, String acctID, CardDesignator card, BigDecimal amount, LocalDate date, boolean isDebit)
    {
        super(descr, amount, date, isDebit);
        this.accountID = acctID;
        this.card = card;
    }


}

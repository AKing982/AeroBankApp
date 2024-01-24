package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceHistoryConverter implements Convert
{

    @Override
    public BalanceHistoryEntity getConversion(com.example.aerobankapp.workbench.history.BalanceHistory balanceHistory)
    {
        Long id = (long) balanceHistory.getId();
        final String acctID = balanceHistory.getAcctID();
        final String user = balanceHistory.getUser();
        final int transactionID = balanceHistory.getTransactionID();
        final BigDecimal balance = balanceHistory.getBalance();
        final BigDecimal adjusted = balanceHistory.getAdjusted();
        final BigDecimal last_balance = balanceHistory.getLastBalance();
        final LocalDate posted = balanceHistory.getPosted();

        return null;
    }
}

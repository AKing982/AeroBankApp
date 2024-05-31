package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.AccountDetails;
import com.example.aerobankapp.model.TransactionCriteria;

import java.math.BigDecimal;
import java.util.List;

public interface AvailableBalanceCalculator
{
    BigDecimal calculateAvailableBalance(AccountDetails accountDetails, List<TransactionCriteria> transactionCriteriaList);
}

package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.AccountDetails;
import com.example.aerobankapp.model.TransactionCriteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PendingBalanceCalculatorEngineImpl implements PendingCalculation
{


    @Override
    public BigDecimal calculatePendingAmount(AccountDetails accountDetails, List<TransactionCriteria> transactionCriteriaList) {
        return null;
    }
}

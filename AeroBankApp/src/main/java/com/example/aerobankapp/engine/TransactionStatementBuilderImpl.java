package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.TransactionStatement;
import com.example.aerobankapp.workbench.DescriptionCode;
import com.example.aerobankapp.workbench.transactions.TransactionType;

import java.math.BigDecimal;

public class TransactionStatementBuilderImpl implements TransactionStatementBuilder
{
    private DescriptionCode descriptionCode;
    private final TransactionType transactionType;

    public TransactionStatementBuilderImpl(TransactionType transactionType){
        this.transactionType = transactionType;
    }

    @Override
    public String format(BigDecimal amount) {
        return null;
    }

    @Override
    public String getDescriptionCode(TransactionType transactionType) {
        return null;
    }

    @Override
    public TransactionStatement getTransactionStatement(String description, BigDecimal balance, BigDecimal debit, BigDecimal credit) {
        return null;
    }
}

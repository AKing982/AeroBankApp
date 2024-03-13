package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.TransactionEntity;
import com.example.aerobankapp.model.TransactionStatement;
import com.example.aerobankapp.workbench.transactions.TransactionType;

import java.util.List;

public interface TransactionStatementBuilder
{
    List<TransactionEntity> getTransactionsByUserID(int userID);
    TransactionStatement getTransactionStatement();
    String buildTransactionDescriptionByTransactionType(TransactionType transactionType);
}

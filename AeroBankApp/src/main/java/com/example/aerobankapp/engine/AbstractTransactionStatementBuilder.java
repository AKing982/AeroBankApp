package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.PendingTransactionEntity;
import com.example.aerobankapp.entity.TransactionEntity;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.model.TransactionStatement;
import com.example.aerobankapp.services.PendingTransactionsService;
import com.example.aerobankapp.services.TransactionService;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractTransactionStatementBuilder<T extends TransactionBase>
{
    private final TransactionService transactionService;
    private final PendingTransactionsService pendingTransactionsService;

    public AbstractTransactionStatementBuilder(TransactionService transactionService,
                                               PendingTransactionsService pendingTransactionsService)
    {
        this.transactionService = transactionService;
        this.pendingTransactionsService = pendingTransactionsService;
    }


    //TODO: Remove if not needed
    protected List<TransactionEntity> loadAllTransactions(){
        List<TransactionEntity> transactionEntities = transactionService.findAll();
        if(transactionEntities.isEmpty()){
            throw new NonEmptyListRequiredException("No Transactions found in the database.");
        }
        return transactionEntities;
    }

    //TODO: Remove if not needed
    protected List<PendingTransactionEntity> loadAllPendingTransactions(){
        List<PendingTransactionEntity> pendingTransactionEntities = pendingTransactionsService.findAll();
        if(pendingTransactionEntities.isEmpty()){
            throw new NonEmptyListRequiredException("No Pending Transactions found in the database.");
        }
        return pendingTransactionEntities;
    }

    protected abstract BigDecimal getCurrentBalance();
    protected abstract String getFormattedDate(LocalDateTime time);
    protected abstract String getFormattedAmount(BigDecimal amount);
    protected abstract List<T> retrieveTransactionsByType(List<TransactionEntity> transactionEntities);
    protected abstract TransactionStatement buildTransactionStatement(List<T> transactionList);
    protected abstract String buildTransactionDescriptionByTransactionType(TransactionType transactionType);
}

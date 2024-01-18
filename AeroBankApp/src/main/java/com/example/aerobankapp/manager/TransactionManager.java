package com.example.aerobankapp.manager;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

public interface TransactionManager<T extends TransactionBase>
{
    boolean isProcessed(Long transactionID);
    boolean processTransaction(T transaction);
    boolean reverseTransaction(Long transactionID);
    void updateTransactionLimit(int userID, int transactionLimit);
    void cancelTransaction(Long transactionID);
    void updateTransaction(T transaction);
    void sendNotification(T transaction, boolean success);
}

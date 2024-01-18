package com.example.aerobankapp.manager;

import com.example.aerobankapp.dto.PurchaseDTO;
import com.example.aerobankapp.workbench.transactions.Purchase;

import java.util.List;

public interface PurchaseManager extends TransactionManager<Purchase>
{
    PurchaseDTO getPurchases(Long id);

    List<PurchaseDTO> getPurchaseHistory(int userID);

    @Override
    boolean isProcessed(Long transactionID);

    @Override
    boolean processTransaction(Purchase transaction);

    @Override
    boolean reverseTransaction(Long transactionID);

    @Override
    void updateTransactionLimit(int userID, int transactionLimit);

    @Override
    void cancelTransaction(Long transactionID);

    @Override
    void updateTransaction(Purchase transaction);

    @Override
    void sendNotification(Purchase transaction, boolean success);
}

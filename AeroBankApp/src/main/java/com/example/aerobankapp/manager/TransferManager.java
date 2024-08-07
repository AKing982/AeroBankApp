package com.example.aerobankapp.manager;

import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.workbench.transactions.Transfer;

import java.util.List;

public interface TransferManager extends TransactionManager<Transfer>
{
    TransferDTO getTransferRequest(Long transferID);

    List<TransferDTO> getTransferHistory(int userID);

    @Override
    boolean isProcessed(Long transactionID);

    @Override
    boolean processTransaction(Transfer transaction);

    @Override
    boolean reverseTransaction(Long transactionID);

    @Override
    void updateTransactionLimit(int userID, int transactionLimit);

    @Override
    void cancelTransaction(Long transactionID);

    @Override
    void updateTransaction(Transfer transaction);

    @Override
    void sendNotification(Transfer transaction, boolean success);
}

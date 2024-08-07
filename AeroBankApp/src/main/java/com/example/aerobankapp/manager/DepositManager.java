package com.example.aerobankapp.manager;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.workbench.transactions.Deposit;

import java.util.List;

public interface DepositManager extends TransactionManager<Deposit>
{
    DepositDTO getDeposit(Long depositID);

    List<DepositDTO> getDepositHistory(int userID);

    @Override
    boolean reverseTransaction(Long transactionID);

    @Override
    void updateTransactionLimit(int userID, int transactionLimit);

    @Override
    void sendNotification(Deposit transaction, boolean success);

    @Override
    boolean isProcessed(Long transactionID);

    @Override
    boolean processTransaction(Deposit transaction);

    @Override
    void cancelTransaction(Long transactionID);

    @Override
    void updateTransaction(Deposit transaction);
}

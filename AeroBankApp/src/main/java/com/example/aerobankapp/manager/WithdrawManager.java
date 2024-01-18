package com.example.aerobankapp.manager;

import com.example.aerobankapp.dto.WithdrawDTO;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawManager extends TransactionManager<Withdraw>
{
    WithdrawDTO getWithdraw(Long withdrawID);

    List<WithdrawDTO> getWithdrawalHistory(int userID);

    int getWithdrawalLimit(int userID);

    @Override
    void updateTransactionLimit(int userID, int transactionLimit);

    @Override
    void sendNotification(Withdraw transaction, boolean success);

    @Override
    boolean isProcessed(Long transactionID);

    @Override
    boolean processTransaction(Withdraw transaction);

    @Override
    void cancelTransaction(Long transactionID);

    @Override
    void updateTransaction(Withdraw transaction);

    @Override
    boolean reverseTransaction(Long transactionID);
}

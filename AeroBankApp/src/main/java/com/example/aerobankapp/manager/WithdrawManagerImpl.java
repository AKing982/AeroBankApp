package com.example.aerobankapp.manager;

import com.example.aerobankapp.dto.WithdrawDTO;
import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.util.List;

public class WithdrawManagerImpl implements WithdrawManager
{

    @Override
    public WithdrawDTO getWithdraw(Long withdrawID) {
        return null;
    }

    @Override
    public List<WithdrawDTO> getWithdrawalHistory(int userID) {
        return null;
    }

    @Override
    public int getWithdrawalLimit(int userID) {
        return 0;
    }

    @Override
    public void updateTransactionLimit(int userID, int transactionLimit) {

    }

    @Override
    public void sendNotification(Withdraw transaction, boolean success) {

    }

    @Override
    public boolean isProcessed(Long transactionID) {
        return false;
    }

    @Override
    public boolean processTransaction(Withdraw transaction) {
        return false;
    }

    @Override
    public void cancelTransaction(Long transactionID) {

    }

    @Override
    public void updateTransaction(Withdraw transaction) {

    }

    @Override
    public boolean reverseTransaction(Long transactionID) {
        return false;
    }
}

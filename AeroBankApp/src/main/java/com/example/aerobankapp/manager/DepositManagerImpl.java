package com.example.aerobankapp.manager;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.workbench.transactions.Deposit;

import java.util.List;

public class DepositManagerImpl implements DepositManager
{
    @Override
    public DepositDTO getDeposit(Long depositID) {
        return null;
    }

    @Override
    public List<DepositDTO> getDepositHistory(int userID) {
        return null;
    }

    @Override
    public boolean reverseTransaction(Long transactionID) {
        return false;
    }

    @Override
    public void updateTransactionLimit(int userID, int transactionLimit) {

    }

    @Override
    public void sendNotification(Deposit transaction, boolean success) {

    }

    @Override
    public boolean isProcessed(Long transactionID) {
        return false;
    }

    @Override
    public boolean processTransaction(Deposit transaction) {
        return false;
    }

    @Override
    public void cancelTransaction(Long transactionID) {

    }

    @Override
    public void updateTransaction(Deposit transaction) {

    }
}

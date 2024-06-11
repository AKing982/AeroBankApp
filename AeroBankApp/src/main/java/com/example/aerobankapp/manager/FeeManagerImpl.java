package com.example.aerobankapp.manager;

import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public class FeeManagerImpl<T extends TransactionBase> implements FeeManager
{
    @Override
    public FeesDTO getFeesByUser(int userID) {
        return null;
    }

    @Override
    public FeesDTO getAccountFees(String acctID) {
        return null;
    }

    @Override
    public void addFeesToAccount(BigDecimal fee, String acctID) {

    }

    @Override
    public void subtractFeesFromAccount(BigDecimal fees, String acctID) {

    }

    @Override
    public void addFeesToTransaction(BigDecimal transactionFee, TransactionBase transaction) {

    }
}

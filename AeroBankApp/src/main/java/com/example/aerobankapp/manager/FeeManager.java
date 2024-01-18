package com.example.aerobankapp.manager;

import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;

public interface FeeManager<T extends TransactionBase>
{
    FeesDTO getFeesByUser(int userID);
    FeesDTO getAccountFees(String acctID);
    void addFeesToAccount(BigDecimal fee, String acctID);
    void subtractFeesFromAccount(BigDecimal fees, String acctID);
    void addFeesToTransaction(BigDecimal transactionFee, T transaction);

}

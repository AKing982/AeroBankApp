package com.example.aerobankapp.engine;

import com.example.aerobankapp.calculation.WithdrawCalculationEngine;
import com.example.aerobankapp.manager.WithdrawManager;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Queue;


public class WithdrawEngine extends Engine<Withdraw>
{
    private Queue<Withdraw> withdrawQueue;
    private Queue<Withdraw> processedWithdrawals;
    private WithdrawManager withdrawManager;
    private WithdrawCalculationEngine withdrawCalculationEngine;
    private boolean isSingleTransaction;
    public WithdrawEngine(Queue<Withdraw> withdraws)
    {
        this.withdrawQueue = withdraws;
        this.isSingleTransaction = withdraws.size() == 1;
    }


    @Override
    protected BigDecimal calculateTransactionFee() {
        return null;
    }

    @Override
    protected BigDecimal convertCurrency(BigDecimal amount, Currency fromCurrency, Currency toCurrency) {
        return null;
    }

    @Override
    protected BigDecimal calculateInterest(BigDecimal amount, BigDecimal annualInterestRate) {
        return null;
    }

    @Override
    protected BigDecimal calculateTax(BigDecimal amount) {
        return null;
    }

    @Override
    protected TransactionSummary generateTransactionSummary(Withdraw transaction) {
        return null;
    }

    @Override
    protected void storeTransaction(Withdraw transaction) {

    }

    @Override
    protected void createAuditTrail(Withdraw transaction) {

    }

    @Override
    protected void notifyAccountHolder(Withdraw transaction) {

    }


}

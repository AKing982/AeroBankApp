package com.example.aerobankapp.engine;

import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public abstract class Engine<T>
{
    protected abstract BigDecimal calculateTransactionFee();
    protected abstract BigDecimal convertCurrency(BigDecimal amount, Currency fromCurrency, Currency toCurrency);
    protected abstract BigDecimal calculateInterest(BigDecimal amount, BigDecimal annualInterestRate);
    protected abstract BigDecimal calculateTax(BigDecimal amount);
    protected abstract void storeTransaction(T transaction);
    protected abstract void createAuditTrail(T transaction);
    protected abstract void notifyAccountHolder(T transaction);

}

package com.example.aerobankapp.engine;

import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;

import java.math.BigDecimal;
import java.util.Currency;

public class DepositEngine extends Engine<Deposit>
{

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
    protected TransactionSummary generateTransactionSummary(Deposit transaction) {
        return null;
    }

    @Override
    protected void storeTransaction(Deposit transaction) {

    }
}

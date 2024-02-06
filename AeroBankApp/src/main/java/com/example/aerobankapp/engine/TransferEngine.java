package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class TransferEngine extends Engine<TransferDTO>{


    @Override
    protected void processTransaction(List<TransferDTO> transactions) {

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
    protected TransactionSummary generateTransactionSummary(TransferDTO transaction) {
        return null;
    }

    @Override
    protected void storeTransaction(TransferDTO transaction) {

    }
}

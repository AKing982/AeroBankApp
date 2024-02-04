package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DepositEngine extends Engine<Deposit>
{
    private List<Deposit> depositList;
    private DepositQueue depositQueue = new DepositQueue();

    public DepositEngine(List<Deposit> deposits)
    {
        this.depositList = deposits;
    }

    public void initializeDepositQueue(List<Deposit> deposits)
    {
        this.depositQueue.addDepositList(deposits);
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
    protected TransactionSummary generateTransactionSummary(Deposit transaction) {
        return null;
    }

    @Override
    protected void storeTransaction(Deposit transaction) {

    }


}

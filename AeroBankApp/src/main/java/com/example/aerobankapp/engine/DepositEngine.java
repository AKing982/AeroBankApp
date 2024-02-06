package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.SchedulerEngine;
import com.example.aerobankapp.scheduler.SchedulerEngineImpl;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DepositEngine extends Engine<DepositDTO>
{
    private List<DepositDTO> depositList;
    private DepositQueue depositQueue;
    private SchedulerCriteria schedulerCriteria;
    private SchedulerEngine schedulerEngine;
    private CalculationEngine calculationEngine;

    @Autowired
    public DepositEngine(DepositQueue depositQueue, List<DepositDTO> deposits)
    {
        this.depositList = deposits;
        this.depositQueue = depositQueue;
        initializeDepositQueue(deposits);
    }

    public void initializeDepositQueue(List<DepositDTO> deposits)
    {
        this.depositQueue.addAll(deposits);
    }

    private SchedulerCriteria buildSchedulerCriteria(DepositDTO deposit)
    {
        return SchedulerCriteria.builder()
                .scheduledTime(String.valueOf(deposit.timeScheduled()))
                .scheduledDate(deposit.date())
                .scheduleType(deposit.scheduleInterval())
                .schedulerUserID(deposit.userID())
                .priority(1)
                .build();
    }

    @Override
    protected void processTransaction(List<DepositDTO> transactions)
    {
        initializeDepositQueue(transactions);
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
    protected TransactionSummary generateTransactionSummary(DepositDTO transaction) {
        return null;
    }

    @Override
    protected void storeTransaction(DepositDTO transaction) {

    }
}

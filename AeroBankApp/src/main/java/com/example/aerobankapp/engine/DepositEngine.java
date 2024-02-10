package com.example.aerobankapp.engine;

import com.example.aerobankapp.DepositQueue;
import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.SchedulerEngine;
import com.example.aerobankapp.scheduler.SchedulerEngineImpl;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class DepositEngine extends Engine<DepositDTO>
{
    private DepositQueue depositQueue;
    private List<DepositDTO> deposits;
    private List<DepositDTO> processedDeposits;
    private CalculationEngine calculationEngine;
    private Map<String, BigDecimal> accountCodeToBalanceMap;

    @Autowired
    public DepositEngine(DepositQueue depositQueue, CalculationEngine calculationEngine)
    {
        this.depositQueue = depositQueue;
        this.calculationEngine = calculationEngine;
        this.processedDeposits = new ArrayList<>();

    }

    private void handleProcessingError(DepositDTO depositDTO, Exception e)
    {

    }

    public List<DepositDTO> processDepositsInQueue(DepositQueue depositQueue)
    {
        return new ArrayList<>(depositQueue.getAllElements());
    }

    public Map<String, BigDecimal> getProcessedBalances(List<DepositDTO> processedDeposits)
    {
        return null;
    }

    public void updateProcessedDepositList(List<DepositDTO> deposits)
    {
        processedDeposits.addAll(deposits);
    }

    public void updateAccountBalance(BigDecimal balance)
    {

    }

    public List<DepositDTO> processDeposits()
    {
        return null;
    }

    @Override
    protected BigDecimal calculateTransactionFee()
    {
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

    @Override
    protected void createAuditTrail(DepositDTO transaction) {

    }

    @Override
    protected void notifyAccountHolder(DepositDTO transaction) {

    }
}

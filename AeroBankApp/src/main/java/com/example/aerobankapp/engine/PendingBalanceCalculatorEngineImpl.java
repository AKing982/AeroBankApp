package com.example.aerobankapp.engine;

import com.example.aerobankapp.exceptions.InvalidDateException;
import com.example.aerobankapp.model.AccountDetails;
import com.example.aerobankapp.model.TransactionCriteria;
import com.example.aerobankapp.services.TransactionCriteriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Component
public class PendingBalanceCalculatorEngineImpl implements PendingCalculation
{
    private Logger LOGGER = LoggerFactory.getLogger(PendingBalanceCalculatorEngineImpl.class);
    private final BigDecimal ZERO_AMOUNT = BigDecimal.ZERO;

    public PendingBalanceCalculatorEngineImpl(){

    }

    @Override
    public BigDecimal calculatePendingAmount(AccountDetails accountDetails, List<TransactionCriteria> transactionCriteriaList) {
        validateInput(accountDetails, transactionCriteriaList);
        LOGGER.info("Calculating pending amount for accountID: {}", accountDetails.getAcctID());
        BigDecimal pendingAmount = getPendingAmountSum(transactionCriteriaList);
        LOGGER.info("Pending Amount for account {}: {}", accountDetails.getAcctID(), pendingAmount);
        return pendingAmount;
    }

    public BigDecimal getPendingAmountSum(final List<TransactionCriteria> transactionCriteriaList){
        if(transactionCriteriaList.isEmpty()){
            throw new IllegalArgumentException("Unable to retrieve pending amount from empty Transactions List.");
        }
        BigDecimal pendingSum = ZERO_AMOUNT;
        for(TransactionCriteria transactionCriteria : transactionCriteriaList){
            if(transactionCriteria.isPending()){
                BigDecimal amount = transactionCriteria.getAmount();
                if(amount == null){
                    return ZERO_AMOUNT;
                }
                pendingSum = pendingSum.add(amount);
            }
        }
        return pendingSum;
    }

    public BigDecimal getPendingAmountByDateRange(final List<TransactionCriteria> transactionCriteriaList, final LocalDate startDate, final LocalDate endDate){
        if(startDate == null || endDate == null){
            LOGGER.error("Invalid Date Ranges found. StartDate: {}, EndDate: {}", startDate, endDate);
            throw new InvalidDateException("Null StartDate or EndDate parameter has been found.");
        }

        if(transactionCriteriaList.isEmpty()){
            return ZERO_AMOUNT;
        }

        BigDecimal pendingSum = ZERO_AMOUNT;
        for(TransactionCriteria criteria : transactionCriteriaList){
            if(criteria.isPending()){
                validateTransactionCriteria(criteria);

                LocalDate scheduledDate = criteria.getScheduledDate();
                BigDecimal amount = criteria.getAmount();

                if (isWithinDateRange(scheduledDate, startDate, endDate)) {
                    pendingSum = getSum(amount, pendingSum);
                    LOGGER.info("Pending Sum: {}", pendingSum);
                }
            }
        }
        return pendingSum;
    }

    private void validateTransactionCriteria(TransactionCriteria criteria) {
        if (criteria.getScheduledDate() == null) {
            throw new InvalidDateException("Unable to retrieve the pending amount using null scheduled date.");
        }
        if (criteria.getAmount() == null) {
            throw new IllegalArgumentException("Unable to retrieve the pending amount from null amount.");
        }
    }

    private boolean isWithinDateRange(LocalDate scheduledDate, LocalDate startDate, LocalDate endDate) {
        return (scheduledDate.isAfter(startDate) && scheduledDate.isBefore(endDate)) ||
                (scheduledDate.equals(startDate) && scheduledDate.isBefore(endDate)) ||
                (scheduledDate.isAfter(startDate) && scheduledDate.equals(endDate));
    }

    private BigDecimal getSum(BigDecimal amount, BigDecimal pending){
        return pending.add(amount);
    }

    public BigDecimal getPendingAmountByCurrency(final List<TransactionCriteria> transactionCriteriaList, final Currency targetCurrency){
        return BigDecimal.ZERO;
    }

    private void validateInput(final AccountDetails accountDetails, final List<TransactionCriteria> transactionCriteriaList){
        if(accountDetails == null){
            LOGGER.error("Account Details must not be null");
            throw new IllegalArgumentException("Invalid Account Details found.");
        }

        if(transactionCriteriaList == null){
            LOGGER.error("Transaction Criteria List must not be null.");
            throw new IllegalArgumentException("Found Null TransactionCriteria List");
        }

        if(transactionCriteriaList.isEmpty()){
            LOGGER.error("Transaction Criteria List cannot be empty.");
        }

        for(TransactionCriteria transactionCriteria1 : transactionCriteriaList){
            if(transactionCriteria1.getAmount() == null ||
              transactionCriteria1.getDescription() == null ||
              transactionCriteria1.getScheduledTime() == null ||
              transactionCriteria1.getScheduledDate() == null ||
              transactionCriteria1.getScheduleType() == null ||
              transactionCriteria1.getCurrency() == null){
                LOGGER.error("Found Null Transaction Parameters. Amount: {}, Description: {}, ScheduledDate: {}, ScheduledTime: {}, ScheduledType: {}, Currency: {}",
                        transactionCriteria1.getAmount(), transactionCriteria1.getDescription(), transactionCriteria1.getScheduledDate(),
                        transactionCriteria1.getScheduledTime(), transactionCriteria1.getScheduleType(), transactionCriteria1.getCurrency());
            }
        }
    }
}

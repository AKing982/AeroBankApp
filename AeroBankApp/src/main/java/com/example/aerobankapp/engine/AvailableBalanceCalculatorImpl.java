package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.AccountDetails;
import com.example.aerobankapp.model.TransactionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AvailableBalanceCalculatorImpl implements AvailableBalanceCalculator
{
    private Logger LOGGER = LoggerFactory.getLogger(AvailableBalanceCalculatorImpl.class);
    private final BigDecimal ZERO = BigDecimal.ZERO;

    public AvailableBalanceCalculatorImpl(){

    }

    @Override
    public BigDecimal calculateAvailableBalance(AccountDetails accountDetails, List<TransactionCriteria> transactionCriteriaList) {
        if(accountDetails == null){
            throw new NullPointerException("Unable to calculate available balance from null account details.");
        }

        if(transactionCriteriaList.isEmpty()){
            return ZERO;
        }
        BigDecimal currentBalance = accountDetails.getBalance();
        if(currentBalance == null){
            LOGGER.error("Found no balance for accountID: {}", accountDetails.getAcctID());
            throw new IllegalArgumentException("Unable to retrieve balance for accountID: " + accountDetails.getAcctID());
        }
        for(TransactionCriteria criteria : transactionCriteriaList){
            if(criteria.isPending()){
                BigDecimal amount = criteria.getAmount();
                if(amount != null){
                    currentBalance = currentBalance.subtract(amount);
                }else{
                    throw new IllegalArgumentException("Invalid Amount found. Unable to proceed calculating available balance.");
                }
            }
        }
        return currentBalance;
    }

    private BigDecimal getBalance(AccountDetails accountDetails){
        return accountDetails.getBalance();
    }

}

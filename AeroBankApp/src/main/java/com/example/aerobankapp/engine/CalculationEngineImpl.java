package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BillDTO;
import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CalculationEngineImpl implements CalculationEngine
{

    private final Logger LOGGER = LoggerFactory.getLogger(CalculationEngineImpl.class);

    public CalculationEngineImpl()
    {

    }

    @Override
    @Transactional
    public BigDecimal calculateDeposit(BigDecimal amount, BigDecimal balance)
    {
        LOGGER.debug("Current Balance: $" + balance);
        if(amount != null && balance != null)
        {
            BigDecimal newBalance = balance.add(amount);
            LOGGER.debug("Balance after Deposit: $" + newBalance);
            return newBalance;
        }
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public BigDecimal calculateWithdrawal(BigDecimal amount, BigDecimal balance) {
        LOGGER.info("Current Balance: $" + balance);
        if(amount != null && balance != null){
            BigDecimal newBalance = balance.subtract(amount);
            LOGGER.info("Balance After Withdrawal: $" + newBalance);
            return newBalance;
        }
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public BigDecimal calculateTransfer(final BigDecimal amount, final Account toAccount, final Account fromAccount) {
        if (amount != null && toAccount != null && fromAccount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            // Retrieve the Account Balances
            BigDecimal fromAccountBalance = fromAccount.getBalance();

            // Check fromAccount balance is not null and has sufficient balance
            if (fromAccountBalance != null && fromAccountBalance.compareTo(amount) >= 0) {
                // Calculate the new balance of the fromAccount after the transfer
                return fromAccountBalance.subtract(amount);
            } else {
                // If fromAccount balance is insufficient or null, throw an exception
                throw new IllegalArgumentException("Insufficient funds or null balance in the fromAccount");
            }
        } else {
            // If any input is null or amount is non-positive, throw an exception
            throw new IllegalArgumentException("Invalid input provided");
        }
    }

    @Override
    @Transactional
    public BigDecimal calculatePurchase(BigDecimal amount, AccountDTO accountDTO) {
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateBillPay(BigDecimal amount, AccountDTO accountDTO, BillDTO bill) {
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateDepositWithInterest(BigDecimal amount, BigDecimal interest, AccountDTO accountDTO) {
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateDepositWithCompoundInterest(BigDecimal amount, BigDecimal interest) {
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateWithdrawalWithFee(BigDecimal amount, AccountDTO accountDTO, FeesDTO feesDTO) {
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateWithdrawalWithInterest(BigDecimal amount, AccountDTO accountDTO, BigDecimal interest) {
        return null;
    }

    @Override
    public BigDecimal calculatePendingBalance(BigDecimal amount, BigDecimal currBalance) {
        return null;
    }

    @Override
    public BigDecimal getAdjustedAmount(BigDecimal currBalance, BigDecimal newBalance) {
        if(currBalance == null || newBalance == null){
            throw new IllegalArgumentException("Invalid Balances have been retrieved.");
        }
        return newBalance.subtract(currBalance);
    }
}

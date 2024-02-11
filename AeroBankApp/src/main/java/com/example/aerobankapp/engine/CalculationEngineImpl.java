package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BillDTO;
import com.example.aerobankapp.fees.FeesDTO;
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
        LOGGER.debug("Current Balance: " + balance);
        if(amount != null && balance != null)
        {
            BigDecimal newBalance = balance.add(amount);
            LOGGER.debug("Balance after Deposit: " + newBalance);
            return newBalance;
        }
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateWithdrawal(BigDecimal amount, BigDecimal balance) {
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateTransfer(BigDecimal amount, AccountDTO toAccount, AccountDTO fromAccount) {
        return null;
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
}

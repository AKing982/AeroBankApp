package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BillDTO;
import com.example.aerobankapp.fees.FeesDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CalculationEngineImpl implements CalculationEngine
{

    @Override
    @Transactional
    public BigDecimal calculateDeposit(BigDecimal amount, AccountDTO accountDTO) {
        return null;
    }

    @Override
    @Transactional
    public BigDecimal calculateWithdrawal(BigDecimal amount, AccountDTO account) {
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
}

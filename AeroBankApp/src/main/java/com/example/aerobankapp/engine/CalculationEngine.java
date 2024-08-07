package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BillDTO;
import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.model.Account;

import java.math.BigDecimal;


public interface CalculationEngine
{
    BigDecimal calculateDeposit(BigDecimal amount, BigDecimal balance);
    BigDecimal calculateWithdrawal(BigDecimal amount, BigDecimal balance);
    BigDecimal calculateTransfer(BigDecimal amount, Account toAccount, Account fromAccount);
    BigDecimal calculatePurchase(BigDecimal amount, AccountDTO accountDTO);
    BigDecimal calculateBillPay(BigDecimal amount, AccountDTO accountDTO, BillDTO bill);
    BigDecimal calculateDepositWithInterest(BigDecimal amount, BigDecimal interest, AccountDTO accountDTO);
    BigDecimal calculateDepositWithCompoundInterest(BigDecimal amount, BigDecimal interest);
    BigDecimal calculateWithdrawalWithFee(BigDecimal amount, AccountDTO accountDTO, FeesDTO feesDTO);
    BigDecimal calculateWithdrawalWithInterest(BigDecimal amount, AccountDTO accountDTO, BigDecimal interest);
    BigDecimal calculatePendingBalance(BigDecimal amount, BigDecimal currBalance);
    BigDecimal getAdjustedAmount(BigDecimal currBalance, BigDecimal newBalance);

}

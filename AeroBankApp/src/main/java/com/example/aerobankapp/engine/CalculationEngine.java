package com.example.aerobankapp.engine;

import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.AccountDetailsDTO;
import com.example.aerobankapp.dto.BillDTO;
import com.example.aerobankapp.fees.FeesDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface CalculationEngine
{
    BigDecimal calculateDeposit(BigDecimal amount, AccountDTO accountDTO);
    BigDecimal calculateWithdrawal(BigDecimal amount, AccountDTO account);
    BigDecimal calculateTransfer(BigDecimal amount, AccountDTO toAccount, AccountDTO fromAccount);
    BigDecimal calculatePurchase(BigDecimal amount, AccountDTO accountDTO);
    BigDecimal calculateBillPay(BigDecimal amount, AccountDTO accountDTO, BillDTO bill);
    BigDecimal calculateDepositWithInterest(BigDecimal amount, BigDecimal interest, AccountDTO accountDTO);
    BigDecimal calculateDepositWithCompoundInterest(BigDecimal amount, BigDecimal interest);
    BigDecimal calculateWithdrawalWithFee(BigDecimal amount, AccountDTO accountDTO, FeesDTO feesDTO);
    BigDecimal calculateWithdrawalWithInterest(BigDecimal amount, AccountDTO accountDTO, BigDecimal interest);

}

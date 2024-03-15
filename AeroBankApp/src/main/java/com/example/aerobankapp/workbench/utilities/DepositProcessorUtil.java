package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.model.DepositBalanceSummary;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DepositProcessorUtil
{
    public static DepositBalanceSummary buildDepositBalanceSummary(final Deposit deposit, final BigDecimal balanceAfterDeposit){
        DepositBalanceSummary depositBalanceSummary = new DepositBalanceSummary();
        depositBalanceSummary.setTransaction(deposit);
        depositBalanceSummary.setPostBalance(balanceAfterDeposit);
        depositBalanceSummary.setDateProcessed(LocalDate.now());
        return depositBalanceSummary;
    }

    public static Deposit convertDepositEntityToDeposit(final DepositsEntity depositsEntity){
        Deposit deposit = new Deposit();
        deposit.setDepositID(depositsEntity.getDepositID());
        deposit.setScheduleInterval(depositsEntity.getScheduleInterval());
        deposit.setDateScheduled(depositsEntity.getScheduledDate());
        deposit.setDescription(depositsEntity.getDescription());
        deposit.setAcctCode(depositsEntity.getAccount().getAccountCode());
        deposit.setUserID(depositsEntity.getUser().getUserID());
        deposit.setAmount(depositsEntity.getAmount());
        deposit.setTimeScheduled(depositsEntity.getScheduledTime());
        deposit.setAccountID(depositsEntity.getAccount().getAcctID());
        deposit.setDate_posted(LocalDate.now());
        return deposit;
    }

    public static TransactionDetail convertToTransactionDetail(Deposit deposit, BigDecimal newBalance) {
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setTransactionID(deposit.getDepositID());
        transactionDetail.setAccountID(deposit.getAccountID());
        transactionDetail.setUserID(deposit.getUserID());
        transactionDetail.setAccountCode(deposit.getAcctCode());
        transactionDetail.setBalance(newBalance);
        return transactionDetail;
    }


}

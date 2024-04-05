package com.example.aerobankapp.engine;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.model.DepositBalanceSummary;
import com.example.aerobankapp.model.TransferBalanceSummary;
import com.example.aerobankapp.model.WithdrawBalanceSummary;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionEngineUtil
{
    public static TransferBalanceSummary buildTransferBalanceSummary(Transfer transfer, BigDecimal postBalance, BigDecimal toAccountPostBalance){
        TransferBalanceSummary transferBalanceSummary = new TransferBalanceSummary();
        transferBalanceSummary.setTransaction(transfer);
        transferBalanceSummary.setPostBalance(postBalance);
        transferBalanceSummary.setToAccountPostBalance(toAccountPostBalance);
        transferBalanceSummary.setDateProcessed(LocalDate.now());
        return transferBalanceSummary;
    }

    public static DepositBalanceSummary buildDepositBalanceSummary(final Deposit deposit, final BigDecimal balanceAfterDeposit){
        DepositBalanceSummary depositBalanceSummary = new DepositBalanceSummary();
        depositBalanceSummary.setTransaction(deposit);
        depositBalanceSummary.setPostBalance(balanceAfterDeposit);
        depositBalanceSummary.setDateProcessed(LocalDate.now());
        return depositBalanceSummary;
    }

    public static WithdrawBalanceSummary buildWithdrawBalanceSummary(final Withdraw withdraw, final BigDecimal balanceAfterWithdraw){
         WithdrawBalanceSummary withdrawBalanceSummary = new WithdrawBalanceSummary();
         withdrawBalanceSummary.setTransaction(withdraw);
         withdrawBalanceSummary.setPostBalance(balanceAfterWithdraw);
         withdrawBalanceSummary.setDateProcessed(LocalDate.now());
         return withdrawBalanceSummary;
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
        deposit.setPosted(LocalDate.now());
        return deposit;
    }
}
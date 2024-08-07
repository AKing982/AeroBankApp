package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.workbench.transactions.Deposit;

import java.time.LocalDate;

public class DepositConverter implements EntityToModelConverter<DepositsEntity, Deposit>
{

    public DepositConverter(){

    }

    @Override
    public Deposit convert(DepositsEntity depositsEntity) {
        Deposit deposit = new Deposit();
        deposit.setDepositID(depositsEntity.getDepositID());
        deposit.setScheduleInterval(depositsEntity.getTransactionCriteria().getTransactionScheduleCriteria().getScheduleInterval());
        deposit.setDateScheduled(depositsEntity.getTransactionCriteria().getTransactionScheduleCriteria().getScheduledDate());
        deposit.setDescription(depositsEntity.getTransactionCriteria().getDescription());
       // deposit.setAcctCode(depositsEntity.getAccount().getAccountCode());
        deposit.setUserID(depositsEntity.getUser().getUserID());
        deposit.setAmount(depositsEntity.getTransactionCriteria().getAmount());
        deposit.setTimeScheduled(depositsEntity.getTransactionCriteria().getTransactionScheduleCriteria().getScheduledTime());
        deposit.setAccountID(depositsEntity.getAccount().getAcctID());
        deposit.setPosted(LocalDate.now());
        return deposit;
    }
}

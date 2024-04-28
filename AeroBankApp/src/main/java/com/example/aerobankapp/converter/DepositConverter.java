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
        deposit.setScheduleInterval(depositsEntity.getScheduleInterval());
        deposit.setDateScheduled(depositsEntity.getScheduledDate());
        deposit.setDescription(depositsEntity.getDescription());
       // deposit.setAcctCode(depositsEntity.getAccount().getAccountCode());
        deposit.setUserID(depositsEntity.getUser().getUserID());
        deposit.setAmount(depositsEntity.getAmount());
        deposit.setTimeScheduled(depositsEntity.getScheduledTime());
        deposit.setAccountID(depositsEntity.getAccount().getAcctID());
        deposit.setPosted(LocalDate.now());
        return deposit;
    }
}

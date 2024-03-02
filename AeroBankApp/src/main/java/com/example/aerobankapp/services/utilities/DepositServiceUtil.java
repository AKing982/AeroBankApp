package com.example.aerobankapp.services.utilities;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public final class DepositServiceUtil
{
    private static Logger LOGGER = LoggerFactory.getLogger(DepositServiceUtil.class);

    public static DepositsEntity buildDepositEntity(DepositDTO depositDTO)
    {
        return DepositsEntity.builder()
                .depositID(depositDTO.getDepositID())
                .account(AccountEntity.builder().accountCode(depositDTO.getAccountCode()).acctID(depositDTO.getAccountID()).build())
                .description(depositDTO.getDescription())
                .amount(depositDTO.getAmount())
                .scheduleInterval(depositDTO.getScheduleInterval())
                .user(UserEntity.builder().userID(depositDTO.getUserID()).build())
                .posted(LocalDate.now())
                .scheduledTime(depositDTO.getTimeScheduled())
                .scheduledDate(depositDTO.getDate())
                .build();
    }

    public static Deposit buildDeposit(DepositDTO request)
    {
        Deposit deposit = new Deposit();
        deposit.setDepositID(request.getDepositID());
        deposit.setAcctCode(request.getAccountCode());
        deposit.setAmount(request.getAmount());
        deposit.setDescription(request.getDescription());
        deposit.setAccountID(request.getAccountID());
        deposit.setDateScheduled(request.getDate());
        deposit.setScheduleInterval(request.getScheduleInterval());
        deposit.setTimeScheduled(request.getTimeScheduled());
        deposit.setDate_posted(LocalDate.now());
        deposit.setUserID(request.getUserID());
        return deposit;
    }

    public static SchedulerCriteria buildCriteria(DepositDTO request)
    {
        LOGGER.debug("Request Date: " + request.getDate());
        LOGGER.debug("Request Time: " + request.getTimeScheduled());
        return SchedulerCriteria.builder()
                .scheduledDate(request.getDate())
                .scheduledTime(request.getTimeScheduled())
                .scheduleType(request.getScheduleInterval())
                .priority(1)
                .createdAt(LocalDate.now())
                .build();
    }
}

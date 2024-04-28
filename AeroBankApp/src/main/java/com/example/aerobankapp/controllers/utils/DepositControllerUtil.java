package com.example.aerobankapp.controllers.utils;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.workbench.utilities.DepositRequest;
import com.example.aerobankapp.workbench.utilities.response.DepositResponse;

import java.util.ArrayList;
import java.util.List;

public class DepositControllerUtil
{
    public static DepositResponse getDepositResponse(final DepositDTO request)
    {
        return DepositResponse.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .accountID(request.getAccountID())
                .accountCode(request.getAccountCode())
                .interval(request.getScheduleInterval())
                .scheduledDate(request.getDate())
                .selectedTime(request.getTimeScheduled())
                .build();
    }

    public static List<DepositResponse> getDepositResponseList(List<DepositsEntity> depositsEntityList)
    {
        List<DepositResponse> depositResponseList = new ArrayList<>();
        for(DepositsEntity deposits : depositsEntityList)
        {
            DepositResponse response = buildDepositResponse(deposits);
            depositResponseList.add(response);
        }
        return depositResponseList;
    }

    public static DepositResponse buildDepositResponse(DepositsEntity deposits)
    {
        return DepositResponse.builder()
                .userID(deposits.getUser().getUserID())
                .interval(deposits.getScheduleInterval())
                .selectedTime(deposits.getScheduledTime())
                .amount(deposits.getAmount())
                .description(deposits.getDescription())
                //  .accountCode(deposits.getAccount().getAccountCode())
                .accountID(deposits.getAccount().getAcctID())
                .scheduledDate(deposits.getScheduledDate())
                .build();
    }
}

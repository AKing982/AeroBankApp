package com.example.aerobankapp.controllers.utils;

import com.example.aerobankapp.workbench.utilities.DepositRequest;
import com.example.aerobankapp.workbench.utilities.response.DepositResponse;

public class DepositControllerUtil
{
    public static DepositResponse getDepositResponse(final DepositRequest request)
    {
        return DepositResponse.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .accountCode(request.getAccountCode())
                .interval(request.getScheduleInterval())
                .scheduledDate(request.getDate())
                .selectedTime(request.getTimeScheduled())
                .build();
    }
}

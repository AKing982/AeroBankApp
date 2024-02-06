package com.example.aerobankapp.workbench.utilities.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DepositResponse
{
    private int userID;
    private String accountCode;
    private String amount;
    private String description;
    private String interval;
    private String selectedTime;
    private String scheduledDate;


}

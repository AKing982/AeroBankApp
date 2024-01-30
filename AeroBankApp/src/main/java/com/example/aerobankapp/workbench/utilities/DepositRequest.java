package com.example.aerobankapp.workbench.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest
{
    private String accountCode;
    private String amount;
    private String date;
    private String timeScheduled;
    private String scheduleInterval;
    private String description;
}

package com.example.aerobankapp.workbench.utilities.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class DepositResponse
{
    private String accountCode;
    private String amount;
    private String description;
    private String interval;
    private String selectedTime;
    private String scheduledDate;


}

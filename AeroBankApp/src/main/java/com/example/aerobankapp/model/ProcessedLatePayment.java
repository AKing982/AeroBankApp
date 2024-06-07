package com.example.aerobankapp.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProcessedLatePayment
{
    private LateBillPayment lateBillPayment;
    private LocalDate dateProcessed;

}

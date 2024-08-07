package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FailedBillPayment
{
    private BillPayment billPayment;
    private LocalDate dateFailed;
    private Status status;

    public FailedBillPayment(BillPayment billPayment, LocalDate dateFailed)
    {
        this.billPayment = billPayment;
        this.dateFailed = dateFailed;
    }

    public FailedBillPayment()
    {

    }
}

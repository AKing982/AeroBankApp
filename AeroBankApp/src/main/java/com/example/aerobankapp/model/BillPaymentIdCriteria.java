package com.example.aerobankapp.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BillPaymentIdCriteria
{
    private Long paymentId;
    private Long scheduleId;

    public BillPaymentIdCriteria(Long paymentId, Long scheduleId)
    {
        this.paymentId = paymentId;
        this.scheduleId = scheduleId;
    }

    public BillPaymentIdCriteria()
    {

    }
}

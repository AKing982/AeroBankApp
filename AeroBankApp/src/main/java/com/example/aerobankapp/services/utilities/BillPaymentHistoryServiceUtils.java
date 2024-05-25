package com.example.aerobankapp.services.utilities;

import com.example.aerobankapp.dto.BillPaymentDTO;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BillPaymentHistoryServiceUtils
{
    public static BillPaymentHistoryEntity createBillPaymentHistory(BillPaymentDTO billPaymentDTO){
        return BillPaymentHistoryEntity.builder()
                .lastPayment(null)
                .nextPayment(billPaymentDTO.paymentDate())
                .dateUpdated(LocalDate.now())
                .build();
    }

}

package com.example.aerobankapp.services.utilities;

import com.example.aerobankapp.dto.BillPaymentDTO;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;

public class BillPaymentScheduleServiceUtils
{
    public static BillPaymentScheduleEntity createBillPaymentSchedule(BillPaymentDTO billPaymentDTO, BillPaymentHistoryEntity billPaymentHistory){
        return BillPaymentScheduleEntity.builder()
                .scheduledPaymentDate(billPaymentDTO.paymentDate())
                .autoPayEnabled(billPaymentDTO.autoPayEnabled())
                .scheduleFrequency(billPaymentDTO.paymentSchedule())
                .billPaymentHistory(billPaymentHistory)
                .paymentDueDate(billPaymentDTO.dueDate())
                .scheduleStatus(ScheduleStatus.PENDING)
                .build();
    }

}

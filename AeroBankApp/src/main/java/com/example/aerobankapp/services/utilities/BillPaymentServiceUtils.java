package com.example.aerobankapp.services.utilities;

import com.example.aerobankapp.dto.BillPaymentDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.entity.UserEntity;

import java.time.LocalDate;

public class BillPaymentServiceUtils
{
    public static BillPaymentEntity createBillPayment(BillPaymentDTO billPaymentDTO, BillPaymentScheduleEntity billPaymentSchedule){
        return BillPaymentEntity.builder()
                .paymentAmount(billPaymentDTO.amount())
                .paymentType("ACCOUNT")
                .paymentSchedule(billPaymentSchedule)
                .payeeName(billPaymentDTO.payeeName())
                .user(UserEntity.builder().userID(billPaymentDTO.userID()).build())
                .account(AccountEntity.builder().acctID(billPaymentDTO.acctID()).build())
                .postedDate(LocalDate.now())
                .build();

    }
}

package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.BillPayment;
import org.springframework.data.convert.EntityConverter;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentConverter implements CombineEntityModelConverter<BillPaymentEntity, BillPaymentScheduleEntity, BillPayment>
{
    public BillPaymentConverter() {

    }

    @Override
    public BillPayment convert(BillPaymentEntity entity1, BillPaymentScheduleEntity entity2) {
        BillPayment billPayment = new BillPayment();
        billPayment.setPaymentID(entity1.getPaymentID());
        billPayment.setPaymentAmount(entity1.getPaymentAmount());
        billPayment.setPaymentType(entity1.getPaymentType());
        billPayment.setPosted(entity1.getPostedDate());
        billPayment.setSchedulePaymentID(entity2.getPaymentScheduleID());
        billPayment.setUserID(entity1.getUser().getUserID());
        billPayment.setAutoPayEnabled(entity2.isAutoPayEnabled());
        billPayment.setPayeeName(entity1.getPayeeName());
        billPayment.setScheduledPaymentDate(entity2.getScheduledPaymentDate());
        billPayment.setDueDate(entity2.getPaymentDueDate());
        billPayment.setScheduleFrequency(entity2.getScheduleFrequency());
        billPayment.setScheduleStatus(entity2.getScheduleStatus());
        return billPayment;
    }
}

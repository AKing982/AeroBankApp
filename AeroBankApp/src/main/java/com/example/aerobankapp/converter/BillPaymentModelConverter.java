package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.model.BillPayment;

public class BillPaymentModelConverter implements EntityToModelConverter<BillPaymentEntity, BillPayment>
{
    @Override
    public BillPayment convert(BillPaymentEntity entity) {
        return null;
    }
}

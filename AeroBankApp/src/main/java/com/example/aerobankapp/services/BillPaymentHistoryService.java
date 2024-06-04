package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

public interface BillPaymentHistoryService extends ServiceDAOModel<BillPaymentHistoryEntity>
{
    boolean isPaymentProcessedById(Long id);
}

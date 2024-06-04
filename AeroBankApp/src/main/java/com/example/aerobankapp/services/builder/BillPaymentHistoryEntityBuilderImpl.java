package com.example.aerobankapp.services.builder;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.model.BillPaymentHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BillPaymentHistoryEntityBuilderImpl implements EntityBuilder<BillPaymentHistoryEntity, BillPaymentHistory>
{
    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentHistoryEntityBuilderImpl.class);

    public BillPaymentHistoryEntityBuilderImpl(){

    }

    @Override
    public BillPaymentHistoryEntity createEntity(BillPaymentHistory model) {
        BillPaymentHistoryEntity entity = new BillPaymentHistoryEntity();
        entity.setProcessed(model.isProcessed());
        entity.setLastPayment(model.getLastPaymentDate());
        entity.setNextPayment(model.getNextPaymentDate());
        entity.setDateUpdated(LocalDate.now());
        LOGGER.info("Created BillPaymentHistoryEntity: {}", entity);
        return entity;
    }
}

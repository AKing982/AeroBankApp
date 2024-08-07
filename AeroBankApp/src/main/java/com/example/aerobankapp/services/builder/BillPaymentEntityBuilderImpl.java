package com.example.aerobankapp.services.builder;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BillPaymentEntityBuilderImpl implements EntityBuilder<BillPaymentEntity, BillPayment>
{
    private final BillPaymentScheduleService billPaymentScheduleService;

    @Autowired
    public BillPaymentEntityBuilderImpl(BillPaymentScheduleService billPaymentScheduleService){
        this.billPaymentScheduleService = billPaymentScheduleService;
    }

    @Override
    public BillPaymentEntity createEntity(BillPayment model) {

        BillPaymentScheduleEntity billPaymentScheduleEntity = getBillPaymentSchedule(model);
        BillPaymentEntity billPayment = new BillPaymentEntity();
        billPayment.setPaymentID(model.getPaymentID());
        billPayment.setPaymentAmount(model.getPaymentAmount());
        billPayment.setPaymentType(model.getPaymentType());
        billPayment.setPayeeName(model.getPayeeName());
        billPayment.setProcessed(model.isProcessed());
        billPayment.setPostedDate(model.getPosted());
        billPayment.setPaymentSchedule(billPaymentScheduleEntity);
        billPayment.setAccount(AccountEntity.builder().acctID(model.getAccountCode().getSequence()).build());
        billPayment.setUser(UserEntity.builder().userID(model.getUserID()).build());
        return null;
    }

    private Long getPaymentScheduleID(Long scheduleID){
        Optional<BillPaymentScheduleEntity> billPaymentScheduleEntityOptional = billPaymentScheduleService.findAllById(scheduleID);
        if(billPaymentScheduleEntityOptional.isPresent()){
            return billPaymentScheduleEntityOptional.get().getPaymentScheduleID();
        }
        return 0L;
    }

    @NotNull
    private BillPaymentScheduleEntity getBillPaymentSchedule(BillPayment model) {
        BillPaymentScheduleEntity billPaymentScheduleEntity = new BillPaymentScheduleEntity();
        billPaymentScheduleEntity.setPaymentDueDate(model.getDueDate());
        billPaymentScheduleEntity.setScheduledPaymentDate(model.getScheduledPaymentDate());
        billPaymentScheduleEntity.setScheduleFrequency(model.getScheduleFrequency());
        billPaymentScheduleEntity.setScheduleStatus(model.getScheduleStatus());
        billPaymentScheduleEntity.setAutoPayEnabled(model.isAutoPayEnabled());
        billPaymentScheduleEntity.setPaymentScheduleID(getPaymentScheduleID(model.getSchedulePaymentID()));
        billPaymentScheduleEntity.setBillPaymentHistory(null);
        return billPaymentScheduleEntity;
    }
}

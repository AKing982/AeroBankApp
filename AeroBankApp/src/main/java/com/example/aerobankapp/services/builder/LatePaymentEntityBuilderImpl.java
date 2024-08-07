package com.example.aerobankapp.services.builder;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.LatePaymentEntity;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.repositories.BillPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Component
public class LatePaymentEntityBuilderImpl implements EntityBuilder<LatePaymentEntity, LateBillPayment>
{
    private final BillPaymentRepository billPaymentRepository;

    @Autowired
    public LatePaymentEntityBuilderImpl(BillPaymentRepository billPaymentRepository)
    {
        this.billPaymentRepository = billPaymentRepository;
    }

    @Override
    public LatePaymentEntity createEntity(LateBillPayment model) {
        LatePaymentEntity latePaymentEntity = new LatePaymentEntity();
        latePaymentEntity.setCreatedAt(Date.from(Instant.now()));
        latePaymentEntity.setOriginalDueDate(model.getOriginalDueDate());

        // Fetch the BillPayment Entity using the paymentID in BillPayment
        Optional<BillPaymentEntity> billPaymentEntity = billPaymentRepository.findById(model.getBillPayment().getPaymentID());
        BillPaymentEntity billPayment = billPaymentEntity.get();

        latePaymentEntity.setBillPayment(billPayment);
        latePaymentEntity.setDaysLate(0);
        latePaymentEntity.setLateFee(BigDecimal.ZERO);

        return latePaymentEntity;
    }
}

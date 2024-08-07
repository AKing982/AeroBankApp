package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillPaymentService extends ServiceDAOModel<BillPaymentEntity>
{
    List<BillPaymentEntity> findBillPaymentsByUserID(int userID);

    Optional<BillPaymentEntity> findById(Long id);

    boolean isBillPaymentProcessed(Long id);
}

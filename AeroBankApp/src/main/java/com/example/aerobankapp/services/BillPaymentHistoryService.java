package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillPaymentHistoryService extends ServiceDAOModel<BillPaymentHistoryEntity>
{
    boolean isPaymentProcessedById(Long id);

    @Override
    List<BillPaymentHistoryEntity> findAll();

    @Override
    void save(BillPaymentHistoryEntity obj);

    @Override
    void delete(BillPaymentHistoryEntity obj);

    @Override
    Optional<BillPaymentHistoryEntity> findAllById(Long id);

    Optional<BillPaymentHistoryEntity> findById(Long id);

    @Override
    List<BillPaymentHistoryEntity> findByUserName(String user);

    Optional<BillPaymentHistoryEntity> findPaymentHistoryByPaymentCriteria(LocalDate lastProcessed, LocalDate nextProcessed, boolean isProcessed, Long id);
}

package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPaymentNotificationEntity;
import com.example.aerobankapp.repositories.BillPaymentNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillPaymentNotificationServiceImpl implements BillPaymentNotificationService
{
    private final BillPaymentNotificationRepository billPaymentNotificationRepository;

    @Autowired
    public BillPaymentNotificationServiceImpl(BillPaymentNotificationRepository billPaymentNotificationRepository){
        this.billPaymentNotificationRepository = billPaymentNotificationRepository;
    }

    @Override
    public List<BillPaymentNotificationEntity> findAll() {
        return null;
    }

    @Override
    public void save(BillPaymentNotificationEntity obj) {

    }

    @Override
    public void delete(BillPaymentNotificationEntity obj) {

    }

    @Override
    public Optional<BillPaymentNotificationEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<BillPaymentNotificationEntity> findByUserName(String user) {
        return null;
    }
}

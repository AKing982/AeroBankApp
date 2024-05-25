package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.repositories.BillPaymentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillPaymentScheduleServiceImpl implements BillPaymentScheduleService
{
    private final BillPaymentScheduleRepository billPaymentScheduleRepository;

    @Autowired
    public BillPaymentScheduleServiceImpl(BillPaymentScheduleRepository billPaymentScheduleRepository){
        this.billPaymentScheduleRepository = billPaymentScheduleRepository;
    }

    @Override
    public List<BillPaymentScheduleEntity> findAll() {
        return null;
    }

    @Override
    public void save(BillPaymentScheduleEntity obj) {

    }

    @Override
    public void delete(BillPaymentScheduleEntity obj) {

    }

    @Override
    public Optional<BillPaymentScheduleEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<BillPaymentScheduleEntity> findByUserName(String user) {
        return null;
    }
}

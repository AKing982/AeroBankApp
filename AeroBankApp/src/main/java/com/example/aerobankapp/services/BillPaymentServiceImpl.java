package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.repositories.BillPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillPaymentServiceImpl implements BillPaymentService
{
    private BillPaymentRepository billPaymentRepository;

    public BillPaymentServiceImpl(BillPaymentRepository billPaymentRepository){
        this.billPaymentRepository = billPaymentRepository;
    }

    @Override
    public List<BillPaymentEntity> findAll() {
        return null;
    }

    @Override
    public void save(BillPaymentEntity obj) {

    }

    @Override
    public void delete(BillPaymentEntity obj) {

    }

    @Override
    public Optional<BillPaymentEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<BillPaymentEntity> findByUserName(String user) {
        return null;
    }

}

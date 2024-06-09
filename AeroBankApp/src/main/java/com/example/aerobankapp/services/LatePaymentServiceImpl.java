package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.LatePaymentEntity;
import com.example.aerobankapp.repositories.LatePaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LatePaymentServiceImpl implements LatePaymentService
{
    private LatePaymentRepository latePaymentRepository;

    @Autowired
    public LatePaymentServiceImpl(LatePaymentRepository latePaymentRepository){
        this.latePaymentRepository = latePaymentRepository;
    }

    @Override
    public List<LatePaymentEntity> findAll() {
        return List.of();
    }

    @Override
    public void save(LatePaymentEntity obj) {

    }

    @Override
    public void delete(LatePaymentEntity obj) {

    }

    @Override
    public Optional<LatePaymentEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<LatePaymentEntity> findByUserName(String user) {
        return List.of();
    }
}

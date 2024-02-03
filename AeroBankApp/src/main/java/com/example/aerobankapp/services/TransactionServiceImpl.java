package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService
{

    @Override
    public List<TransactionEntity> findAll() {
        return null;
    }

    @Override
    public void save(TransactionEntity obj) {

    }

    @Override
    public void delete(TransactionEntity obj) {

    }

    @Override
    public Optional<TransactionEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TransactionEntity> findByUserName(String user) {
        return null;
    }
}

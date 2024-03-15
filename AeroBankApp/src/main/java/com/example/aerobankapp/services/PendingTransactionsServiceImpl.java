package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.PendingTransactionEntity;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.List;
import java.util.Optional;

@Service
public class PendingTransactionsServiceImpl implements PendingTransactionsService
{

    @Override
    public void save(PendingTransactionEntity obj) {

    }

    @Override
    public void delete(PendingTransactionEntity obj) {

    }

    @Override
    public List<PendingTransactionEntity> findAll() {
        return null;
    }

    @Override
    public Optional<PendingTransactionEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PendingTransactionEntity> findByUserName(String user) {
        return null;
    }
}

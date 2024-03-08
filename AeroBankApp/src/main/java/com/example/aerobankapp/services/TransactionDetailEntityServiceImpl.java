package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionDetailEntity;
import com.example.aerobankapp.repositories.TransactionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionDetailEntityServiceImpl implements TransactionDetailEntityService
{
    private final TransactionDetailRepository transactionDetailRepository;

    @Autowired
    public TransactionDetailEntityServiceImpl(TransactionDetailRepository transactionDetailRepository)
    {
        this.transactionDetailRepository = transactionDetailRepository;
    }

    @Override
    public List<TransactionDetailEntity> findAll() {
        return null;
    }

    @Override
    public void save(TransactionDetailEntity obj) {

    }

    @Override
    public void delete(TransactionDetailEntity obj) {

    }

    @Override
    public Optional<TransactionDetailEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TransactionDetailEntity> findByUserName(String user) {
        return null;
    }
}

package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionCriteriaEntity;
import com.example.aerobankapp.repositories.TransactionCriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionCriteriaServiceImpl implements TransactionCriteriaService
{
    private TransactionCriteriaRepository transactionCriteriaRepository;

    @Autowired
    public TransactionCriteriaServiceImpl(TransactionCriteriaRepository transactionCriteriaRepository)
    {
        this.transactionCriteriaRepository = transactionCriteriaRepository;
    }

    @Override
    public List<TransactionCriteriaEntity> findAll() {
        return null;
    }

    @Override
    public void save(TransactionCriteriaEntity obj) {
        transactionCriteriaRepository.save(obj);
    }

    @Override
    public void delete(TransactionCriteriaEntity obj) {

    }

    @Override
    public Optional<TransactionCriteriaEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TransactionCriteriaEntity> findByUserName(String user) {
        return null;
    }
}

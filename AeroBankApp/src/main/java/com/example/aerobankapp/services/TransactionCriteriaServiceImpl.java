package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionCriteriaEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionCriteriaServiceImpl implements TransactionCriteriaService
{

    @Override
    public List<TransactionCriteriaEntity> findAll() {
        return null;
    }

    @Override
    public void save(TransactionCriteriaEntity obj) {

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

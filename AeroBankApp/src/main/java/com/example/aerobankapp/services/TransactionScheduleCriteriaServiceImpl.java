package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionScheduleCriteriaEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionScheduleCriteriaServiceImpl implements TransactionScheduleCriteriaService
{

    @Override
    public List<TransactionScheduleCriteriaEntity> findAll() {
        return null;
    }

    @Override
    public void save(TransactionScheduleCriteriaEntity obj) {

    }

    @Override
    public void delete(TransactionScheduleCriteriaEntity obj) {

    }

    @Override
    public Optional<TransactionScheduleCriteriaEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TransactionScheduleCriteriaEntity> findByUserName(String user) {
        return null;
    }
}
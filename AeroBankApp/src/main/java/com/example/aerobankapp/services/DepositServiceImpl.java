package com.example.aerobankapp.services;

import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.repositories.DepositRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepositServiceImpl implements DepositService
{
    private final DepositRepository depositRepository;
    private final DepositEngine depositEngine;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DepositServiceImpl(DepositRepository depositRepository,
                              DepositEngine depositEngine,
                              EntityManager entityManager)
    {
        this.depositRepository = depositRepository;
        this.entityManager = entityManager;
        this.depositEngine = depositEngine;
    }

    @Override
    public List<DepositsEntity> findAll() {
        return null;
    }

    @Override
    public void save(DepositsEntity obj) {

    }

    @Override
    public void delete(DepositsEntity obj) {

    }

    @Override
    public Optional<DepositsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DepositsEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getDepositsByUserNameDesc(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserIDASC(Long id) {
        return null;
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserID_DESC(Long id) {
        return null;
    }
}

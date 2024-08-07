package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.DepositQueueEntity;
import com.example.aerobankapp.repositories.DepositQueueRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositQueueServiceImpl implements DepositQueueService
{
    private DepositQueueRepository depositQueueRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DepositQueueServiceImpl(DepositQueueRepository depositQueueRepository, EntityManager entityManager)
    {
        this.depositQueueRepository = depositQueueRepository;
        this.entityManager = entityManager;
    }

    @Override
    public void save(DepositQueueEntity transaction)
    {
        depositQueueRepository.save(transaction);
    }

    @Override
    public DepositQueueEntity enQueue(DepositQueueEntity transaction)
    {
        return null;
    }

    @Override
    public DepositQueueEntity processNext() {
        return null;
    }

    @Override
    public List<DepositQueueEntity> processAll() {
        return null;
    }

    @Override
    public List<DepositQueueEntity> getCurrentQueue() {
        return null;
    }

    @Override
    public DepositQueueEntity getTransactionFromQueue(Long queueID) {
        return null;
    }

    @Override
    public DepositQueueEntity deQueueTransaction(Long id) {
        return null;
    }
}

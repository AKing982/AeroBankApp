package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.DepositQueueEntity;

import java.util.List;

public interface DepositQueueService extends QueueService<DepositQueueEntity>
{

    @Override
    DepositQueueEntity enQueue(DepositQueueEntity transaction);

    @Override
    DepositQueueEntity processNext();

    @Override
    List<DepositQueueEntity> processAll();

    @Override
    List<DepositQueueEntity> getCurrentQueue();

    @Override
    DepositQueueEntity getTransactionFromQueue(Long queueID);

    @Override
    DepositQueueEntity deQueueTransaction(Long id);
}

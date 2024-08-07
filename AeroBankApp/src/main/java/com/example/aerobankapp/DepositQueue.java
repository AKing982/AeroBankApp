package com.example.aerobankapp;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.DepositQueueEntity;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.services.DepositQueueService;
import com.example.aerobankapp.services.DepositQueueServiceImpl;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.utilities.QueueStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class DepositQueue implements QueueModel<Deposit>
{
    private final Queue<Deposit> queue = new ConcurrentLinkedQueue<>();
    private int queueSize;
    private final int maxQueueSize = 150;
    private DepositQueueService depositQueueService;
    private boolean isPersisting;
    private Logger LOGGER = LoggerFactory.getLogger(DepositQueue.class);

    @Autowired
    public DepositQueue(@Qualifier("depositQueueServiceImpl")DepositQueueService depositQueueService)
    {
        this.depositQueueService = depositQueueService;
    }


    @Override
    public List<Deposit> getAllElements()
    {
        LOGGER.info("Queue Size: " + queue.size());
        return new ArrayList<>(queue);
    }

    @Override
    public void add(Deposit transaction)
    {
        if(transaction == null)
        {
            throw new NullPointerException("Null Deposit Found in Queue.");
        }
        queue.add(transaction);
    }

    private DepositsEntity convertToDepositEntity(Deposit deposit)
    {
        return DepositsEntity.builder()
                .depositID(deposit.getDepositID())
         //       .transactionCriteria(deposit.getAmount())
          //      .description(deposit.getDescription())
          //      .scheduledDate(deposit.getDateScheduled())
          //      .scheduledTime(deposit.getTimeScheduled())
          //      .posted(deposit.getPosted())
         //       .scheduleInterval(deposit.getScheduleInterval())
         //       .user(UserEntity.builder().userID(1).build())
             //   .account(AccountEntity.builder().accountCode(deposit.getAcctCode()).build())
                .build();
    }

    private DepositQueueEntity convertToQueueEntity(DepositsEntity deposits)
    {
        return DepositQueueEntity.builder()
                .deposit(deposits)
                .queuedAt(Timestamp.from(Instant.now()))
                .status(QueueStatus.PENDING)
                .build();
    }


    @Override
    public void addAll(List<Deposit> transactions)
    {
        queue.addAll(transactions);
    }

    @Override
    public void addToDatabase(Deposit transaction)
    {
        DepositsEntity depositsEntity = convertToDepositEntity(transaction);
        DepositQueueEntity depositQueueEntity = convertToQueueEntity(depositsEntity);
        depositQueueService.save(depositQueueEntity);
    }

    @Override
    public void addAllToDatabase(List<Deposit> transactions)
    {
        for(Deposit depositDTO : transactions)
        {
            DepositsEntity depositsEntity = convertToDepositEntity(depositDTO);
            DepositQueueEntity depositQueueEntity = convertToQueueEntity(depositsEntity);
            depositQueueService.save(depositQueueEntity);
        }
    }

    @Override
    public Deposit remove()
    {
       return queue.remove();
    }

    @Override
    public Deposit removeFromDatabase() {
        return null;
    }

    @Override
    public Deposit peek()
    {
        return queue.peek();
    }

    private DepositQueueEntity dequeueFromDatabase(Deposit depositDTO)
    {
        Long depositID = (long) depositDTO.getDepositID();
        return depositQueueService.deQueueTransaction(depositID);
    }

    @Override
    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    @Override
    public boolean isDuplicate(Deposit element)
    {
        int count = 0;
        for(Deposit deposit : queue)
        {
            if(deposit.equals(element))
            {
                count++;
                if(count > 1)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Deposit poll() {
        return queue.poll();
    }

    public int size()
    {
        return queue.size();
    }
}

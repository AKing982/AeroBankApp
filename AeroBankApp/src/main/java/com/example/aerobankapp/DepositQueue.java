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
public class DepositQueue implements QueueModel<DepositDTO>
{
    private final Queue<DepositDTO> queue = new ConcurrentLinkedQueue<>();
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
    public List<DepositDTO> getAllElements()
    {
        LOGGER.info("Queue Size: " + queue.size());
        return new ArrayList<>(queue);
    }

    @Override
    public void add(DepositDTO transaction)
    {
        if(transaction == null)
        {
            throw new NullPointerException("Null Deposit Found in Queue.");
        }
        queue.add(transaction);
    }

    private DepositsEntity convertToDepositEntity(DepositDTO depositDTO)
    {
        return DepositsEntity.builder()
                .depositID(depositDTO.getDepositID())
                .amount(depositDTO.getAmount())
                .description(depositDTO.getDescription())
                .scheduledDate(depositDTO.getDate())
                .scheduledTime(depositDTO.getTimeScheduled())
                .posted(depositDTO.getDate())
                .scheduleInterval(depositDTO.getScheduleInterval())
                .user(UserEntity.builder().userID(1).build())
                .account(AccountEntity.builder().accountCode(depositDTO.getAccountCode()).build())
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
    public void addAll(List<DepositDTO> transactions)
    {
        queue.addAll(transactions);
    }

    @Override
    public void addToDatabase(DepositDTO transaction)
    {
        DepositsEntity depositsEntity = convertToDepositEntity(transaction);
        DepositQueueEntity depositQueueEntity = convertToQueueEntity(depositsEntity);
        depositQueueService.save(depositQueueEntity);
    }

    @Override
    public void addAllToDatabase(List<DepositDTO> transactions)
    {
        for(DepositDTO depositDTO : transactions)
        {
            DepositsEntity depositsEntity = convertToDepositEntity(depositDTO);
            DepositQueueEntity depositQueueEntity = convertToQueueEntity(depositsEntity);
            depositQueueService.save(depositQueueEntity);
        }
    }

    @Override
    public DepositDTO remove()
    {
       return queue.remove();
    }

    @Override
    public DepositDTO removeFromDatabase() {
        return null;
    }

    @Override
    public DepositDTO peek()
    {
        return queue.peek();
    }

    private DepositQueueEntity dequeueFromDatabase(DepositDTO depositDTO)
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
    public boolean isDuplicate(DepositDTO element)
    {
        int count = 0;
        for(DepositDTO depositDTO : queue)
        {
            if(depositDTO.equals(element))
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
    public DepositDTO poll() {
        return queue.poll();
    }

    public int size()
    {
        return queue.size();
    }
}

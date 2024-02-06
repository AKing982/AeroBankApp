package com.example.aerobankapp;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.services.DepositQueueService;
import com.example.aerobankapp.services.DepositQueueServiceImpl;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class DepositQueue implements QueueModel<DepositDTO>
{
    private final Queue<DepositDTO> queue = new ConcurrentLinkedQueue<>();
    private DepositQueueService depositQueueService;

    @Autowired
    public DepositQueue(@Qualifier("depositQueueServiceImpl")DepositQueueService depositQueueService)
    {
        this.depositQueueService = depositQueueService;
    }

    @Override
    public void add(DepositDTO transaction)
    {
        queue.add(transaction);
        saveToDatabase(transaction);
    }

    @Override
    public void addAll(List<DepositDTO> transactions)
    {
        queue.addAll(transactions);
        saveBatchToDatabase(transactions);
    }

    @Override
    public DepositDTO remove()
    {
        DepositDTO depositDTO = queue.remove();
        removeFromDatabase(depositDTO);
        return depositDTO;
    }

    @Override
    public DepositDTO peek()
    {
        return queue.peek();
    }

    private void saveToDatabase(DepositDTO depositDTO)
    {

    }

    private void saveBatchToDatabase(List<DepositDTO> depositDTOS)
    {

    }

    private void removeFromDatabase(DepositDTO depositDTO)
    {

    }

    @Override
    public boolean isEmpty()
    {
        return queue.isEmpty();
    }
}

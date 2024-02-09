package com.example.aerobankapp;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.services.DepositQueueService;
import com.example.aerobankapp.services.DepositQueueServiceImpl;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class DepositQueue implements QueueModel<DepositDTO>
{
    private final Queue<DepositDTO> queue = new ConcurrentLinkedQueue<>();
    private DepositQueueService depositQueueService;
    private boolean isPersisting;

    @Autowired
    public DepositQueue(@Qualifier("depositQueueServiceImpl")DepositQueueService depositQueueService)
    {
        this.depositQueueService = depositQueueService;
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

    @Override
    public void addAll(List<DepositDTO> transactions)
    {
        queue.addAll(transactions);
    }

    @Override
    public void addToDatabase(DepositDTO transaction) {

    }

    @Override
    public void addAllToDatabase(List<DepositDTO> transactions) {

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

    @Override
    public boolean isDuplicate(DepositDTO element)
    {
        for(DepositDTO depositDTO : queue)
        {
            if(depositDTO.equals(element))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public DepositDTO removeDuplicate(DepositDTO duplicate)
    {
        DepositDTO removed = null;
        for(DepositDTO depositDTO : queue)
        {
            if(depositDTO.equals(duplicate))
            {
                removed = queue.remove();
            }
        }
        return removed;
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

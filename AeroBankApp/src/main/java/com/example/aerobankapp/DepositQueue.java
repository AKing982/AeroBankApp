package com.example.aerobankapp;

import com.example.aerobankapp.workbench.transactions.Deposit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class DepositQueue extends QueueModel<Deposit>
{
    private final ConcurrentLinkedQueue<Deposit> queue = new ConcurrentLinkedQueue<>();

    public void addDeposit(Deposit deposit)
    {
        queue.add(deposit);
    }

    public void addDepositList(List<Deposit> deposits)
    {
        queue.addAll(deposits);
    }


}

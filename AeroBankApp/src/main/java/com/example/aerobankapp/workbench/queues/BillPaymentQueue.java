package com.example.aerobankapp.workbench.queues;

import com.example.aerobankapp.model.BillPayment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Component
public class BillPaymentQueue extends AbstractQueueBase<BillPayment>
{
    public BillPaymentQueue() {
        super();
    }

    public void clear(){
        super.getQueue().clear();
    }

    public Queue<BillPayment> getQueueState(){
        return super.getQueue();
    }

    public boolean contains(BillPayment payment){
        return super.getQueue().contains(payment);
    }

    @Override
    public void add(BillPayment item) {
        super.add(item);
    }

    @Override
    public void addAll(List<BillPayment> items) {
        super.addAll(items);
    }

    @Override
    public void remove(BillPayment item) {
        super.remove(item);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public Queue<BillPayment> getQueue() {
        return super.getQueue();
    }

    @Override
    public BillPayment poll() {
        return super.poll();
    }
}

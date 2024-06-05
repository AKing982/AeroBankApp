package com.example.aerobankapp.workbench.queues;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractQueueBase<T>
{
    private Queue<T> queue;

    public AbstractQueueBase(){
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void add(T item){
        this.queue.add(item);
    }

    public void addAll(List<T> items){
        this.queue.addAll(items);
    }

    public void remove(T item){
        this.queue.remove();
    }

    public boolean isEmpty(){
        return this.queue.isEmpty();
    }

    public int size(){
        return this.queue.size();
    }

    public Queue<T> getQueue(){
        return this.queue;
    }

    public T poll(){
        return this.queue.poll();
    }
}
package com.example.aerobankapp;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface QueueModel<T>
{
    void add(T transaction);
    void addAll(List<T> transactions);

    T remove();
    T peek();
    boolean isEmpty();

}

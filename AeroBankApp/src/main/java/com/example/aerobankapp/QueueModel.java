package com.example.aerobankapp;

import com.example.aerobankapp.dto.DepositDTO;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface QueueModel<T>
{
    List<DepositDTO> getAllElements();
    void add(T transaction);
    void addAll(List<T> transactions);
    void addToDatabase(T transaction);
    void addAllToDatabase(List<T> transactions);
    T remove();
    T removeFromDatabase();
    T peek();
    boolean isEmpty();
    boolean isDuplicate(T element);
    T poll();

}

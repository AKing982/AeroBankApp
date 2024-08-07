package com.example.aerobankapp.services;

import java.util.List;

public interface QueueService<T>
{
    void save(T transaction);


    /**
     * Adds a transaction to the queue.
     *
     * @param transaction The deposit entity to be added to the queue.
     * @return The queued deposit entity.
     */

    T enQueue(T transaction);

    /**
     * Processes the next transaction in the queue.
     *
     * @return The processed transaction entity, or null if the queue is empty.
     */
    T processNext();

    List<T> processAll();

    List<T> getCurrentQueue();

    T getTransactionFromQueue(Long queueID);

    T deQueueTransaction(Long id);
}

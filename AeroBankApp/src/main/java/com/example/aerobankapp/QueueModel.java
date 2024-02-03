package com.example.aerobankapp;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class QueueModel<T>
{
    protected Queue<T> queue = new ConcurrentLinkedQueue<>();


}

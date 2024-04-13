package com.example.aerobankapp.engine;

import com.example.aerobankapp.model.AccountNotification;
import org.springframework.stereotype.Service;

import java.util.PriorityQueue;

@Service
public class AccountNotificationEngine implements Runnable
{
    private PriorityQueue<AccountNotification> notificationQueue;

    @Override
    public void run() {

    }
}

package com.example.aerobankapp;

import com.example.aerobankapp.engine.DepositEngine;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositMessageListener
{
    @Autowired
    private DepositEngine depositEngine;
    @RabbitListener(queues = "depositQueue")
    public void receive(String in) {
        System.out.println("Received message: " + in);
        // Process the deposit here
    }

}

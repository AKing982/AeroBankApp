package com.example.aerobankapp.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class DepositJob implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String depositDetails = "details";

        // Send message to RabbitMQ

    }
}

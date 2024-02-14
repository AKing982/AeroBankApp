package com.example.aerobankapp.scheduler.jobs;

import jakarta.annotation.PostConstruct;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.aerobankapp.configuration.RabbitMQConfig.jackson2JsonMessageConverter;

@Component
public class DepositJob implements Job
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String depositDetails = "Details of the deposit";



        // Sending message to RabbitMQ

        rabbitTemplate.convertAndSend(exchange.getName(), "deposit.process", depositDetails);

    }

    @PostConstruct
    public void postConstruct() {
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
    }
}

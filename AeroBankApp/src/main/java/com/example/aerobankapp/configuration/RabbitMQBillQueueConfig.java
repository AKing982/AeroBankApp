package com.example.aerobankapp.configuration;

import com.example.aerobankapp.model.BillPaymentSchedule;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQBillQueueConfig {

    @Bean
    public Queue billPaymentSchedule(){
        return new Queue("billPaymentScheduleQueue", true);
    }

    @Bean
    public Queue missedPaymentsQueue(){
        return new Queue("missedPaymentsQueue", true);
    }

    @Bean
    public Queue latePaymentsQueue(){
        return new Queue("latePaymentsQueue", true);
    }

    @Bean
    public Queue regularBillPaymentsQueue(){
        return new Queue("regularBillPaymentsQueue", true);
    }
}

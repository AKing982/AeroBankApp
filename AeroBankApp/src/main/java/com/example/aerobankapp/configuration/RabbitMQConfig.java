package com.example.aerobankapp.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{
    @Bean(name="transactionQueue")
    Queue queue() {
        return new Queue("transactionQueue", false);
    }


    @Bean
    TopicExchange exchange() {
        return new TopicExchange("transactionExchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("deposit.#");
    }

}

package com.example.aerobankapp;

import com.example.aerobankapp.dto.ProcessedDepositDTO;
import com.example.aerobankapp.engine.CalculationEngine;
import com.example.aerobankapp.engine.DepositEngine;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepositMessageListener
{
    @Autowired
    private DepositEngine depositEngine;

    @Autowired
    private CalculationEngine calculationEngine;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger LOGGER = LoggerFactory.getLogger(DepositMessageListener.class);

    @RabbitListener(queues = "depositQueue")
    public void receive(String in) {

        try
        {
            DepositQueue depositQueue = objectMapper.readValue(in, DepositQueue.class);

            // Process the deposit here
            depositEngine = new DepositEngine(depositQueue, calculationEngine, accountService, notificationService);

            List<ProcessedDepositDTO> processedDepositDTOList = depositEngine.processDeposits();

        }catch(Exception e)
        {
            LOGGER.error("An exception has occurred while retrieving a message: ", e);
        }

    }

}

package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.*;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;

import com.example.aerobankapp.scheduler.factory.TriggerFactoryImpl;
import com.example.aerobankapp.scheduler.trigger.SchedulerTriggerImpl;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleParser;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleParserImpl;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleValidator;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleValidatorImpl;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class AsyncDepositService
{
    private SchedulerEngine schedulerEngine;
    private ScheduleParserImpl scheduleParser;
    private ScheduleValidator scheduleValidator;
    private TriggerCriteriaService triggerCriteriaService;
    private Logger LOGGER = LoggerFactory.getLogger(AsyncDepositService.class);
    private Scheduler scheduler;
    private final JobDetail depositJobDetail;

    public AsyncDepositService(Scheduler scheduler, @Qualifier("depositJobDetail") JobDetail jobDetail,
                               SchedulerEngine schedulerEngine,
                               ScheduleParserImpl scheduleParser,
                               ScheduleValidator scheduleValidator,
                               TriggerCriteriaService triggerCriteriaService)
    {
        this.scheduler = scheduler;
        this.depositJobDetail = jobDetail;
        this.schedulerEngine = schedulerEngine;
        this.scheduleParser = scheduleParser;
        this.scheduleValidator = scheduleValidator;
        this.triggerCriteriaService = triggerCriteriaService;
    }


    @Async
    public void validateAndParse(SchedulerCriteria schedulerCriteria, Consumer<TriggerCriteria> callback)
    {
        LOGGER.info("Scheduled Date: " + schedulerCriteria.getScheduledDate());
        LOGGER.info("Scheduled Time: " + schedulerCriteria.getScheduledTime());

        //ScheduleParserImpl scheduleParser = new ScheduleParserImpl(schedulerCriteria, scheduleValidator);
        //TriggerCriteria triggerCriteria = scheduleParser.buildTriggerCriteria();

        // Execute the callback with the result
        //callback.accept(triggerCriteria);
    }


    @Async
    public void sendToRabbitMQ(Deposit deposit) {
        LOGGER.info("Sending Deposit to Rabbit");
        //rabbitTemplate.convertAndSend("depositQueue", deposit);
    }

    @Async
    public void startScheduler(TriggerCriteria triggerCriteria) throws SchedulerException {
        LOGGER.info("Starting the Scheduler");
        // Assume TriggerFactory is a Spring @Component
       // TriggerFactoryImpl triggerFactory = new TriggerFactoryImpl(triggerCriteria);
      //  Trigger trigger = triggerFactory.getTriggerInstance();

        // Assume ScheduleRunner is a Spring @Component
    //    schedulerEngine.startScheduler();
     //   schedulerEngine.scheduleJob(depositJobDetail, trigger);
    }
}

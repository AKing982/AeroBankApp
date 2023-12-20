package com.example.aerobankapp.scheduler.factory;

import com.example.aerobankapp.scheduler.factory.trigger.EvenDayCronTriggerFactory;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EvenDaySchedulerFactory implements AbstractSchedulerTypeFactory
{
    private final Scheduler scheduler;
    private final EvenDayCronTriggerFactory evenDayCronTriggerFactory;

    @Autowired
    public EvenDaySchedulerFactory(@Qualifier("scheduler") Scheduler scheduler, @Qualifier("evenDayCronTriggerFactory")EvenDayCronTriggerFactory evenDayCronTriggerFactory)
    {
        this.scheduler = scheduler;
        this.evenDayCronTriggerFactory = evenDayCronTriggerFactory;
    }

    @Override
    public Scheduler createScheduler()
    {
        return null;
    }
}

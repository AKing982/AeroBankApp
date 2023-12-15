package com.example.aerobankapp.scheduler.factory;

import com.example.aerobankapp.scheduler.ScheduleType;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerFactoryProducer
{
    private AbstractSchedulerTypeFactory abstractSchedulerTypeFactory;

    @Autowired
    private Scheduler scheduler;

    public Scheduler getSchedulerFactory(ScheduleType scheduleType)
    {
        switch(scheduleType)
        {
            case DAILY:
                abstractSchedulerTypeFactory = new DailySchedulerFactory(scheduler);
                return abstractSchedulerTypeFactory.createScheduler();
            case WEEKLY:
                abstractSchedulerTypeFactory = new WeeklySchedulerFactory();
                return abstractSchedulerTypeFactory.createScheduler();
            case MONTHLY:
                abstractSchedulerTypeFactory = new MonthlySchedulerFactory(scheduler);
                return abstractSchedulerTypeFactory.createScheduler();
            case BIWEEKLY:
                abstractSchedulerTypeFactory = new BiWeeklySchedulerFactory(scheduler);
                return abstractSchedulerTypeFactory.createScheduler();
            case EVERY_TWO_DAYS:
                abstractSchedulerTypeFactory = new EvenDaySchedulerFactory(scheduler);
                return abstractSchedulerTypeFactory.createScheduler();
            case CUSTOM:
                abstractSchedulerTypeFactory = new CustomSchedulerFactory(scheduler);
                return abstractSchedulerTypeFactory.createScheduler();
            default:
                return null;
        }
    }
}

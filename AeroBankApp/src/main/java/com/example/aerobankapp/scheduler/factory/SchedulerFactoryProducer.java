package com.example.aerobankapp.scheduler.factory;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.factory.trigger.EvenDayCronTriggerFactory;
import com.example.aerobankapp.scheduler.factory.trigger.MonthlyTriggerFactory;
import com.example.aerobankapp.scheduler.factory.trigger.WeeklyTriggerFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerFactoryProducer
{
    private AbstractSchedulerTypeFactory abstractSchedulerTypeFactory;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MonthlyTriggerFactory monthlyTriggerFactory;

    @Autowired
    private WeeklyTriggerFactory weeklyTriggerFactory;

    @Autowired
    private EvenDayCronTriggerFactory evenDayCronTriggerFactory;

    public Scheduler getSchedulerFactory(ScheduleType scheduleType)
    {
        return switch (scheduleType) {
            case DAILY -> {
                abstractSchedulerTypeFactory = new DailySchedulerFactory(scheduler);
                yield abstractSchedulerTypeFactory.createScheduler();
            }
            case WEEKLY -> {
                abstractSchedulerTypeFactory = new WeeklySchedulerFactory(scheduler, weeklyTriggerFactory);
                yield abstractSchedulerTypeFactory.createScheduler();
            }
            case MONTHLY -> {
                abstractSchedulerTypeFactory = new MonthlySchedulerFactory(scheduler, monthlyTriggerFactory);
                yield abstractSchedulerTypeFactory.createScheduler();
            }
            case BIWEEKLY -> {
                abstractSchedulerTypeFactory = new BiWeeklySchedulerFactory(scheduler);
                yield abstractSchedulerTypeFactory.createScheduler();
            }
            case BIDAILY -> {
                abstractSchedulerTypeFactory = new EvenDaySchedulerFactory(scheduler, evenDayCronTriggerFactory);
                yield abstractSchedulerTypeFactory.createScheduler();
            }
            case CUSTOM -> {
                abstractSchedulerTypeFactory = new CustomSchedulerFactory(scheduler);
                yield abstractSchedulerTypeFactory.createScheduler();
            }
            default -> null;
        };
    }
}

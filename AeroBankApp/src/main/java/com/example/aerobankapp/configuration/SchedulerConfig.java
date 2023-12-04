package com.example.aerobankapp.configuration;

import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SchedulerConfig
{
    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Scheduler scheduler() throws SchedulerException
    {
        return new StdSchedulerFactory().getScheduler();
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Trigger weeklyDepositTrigger(DepositJobDetail depositJobDetail)
    {
        return createWeeklyTrigger(depositJobDetail);
    }

}

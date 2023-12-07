package com.example.aerobankapp.configuration;

import com.example.aerobankapp.scheduler.jobdetail.DepositJob;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.*;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import static org.quartz.JobBuilder.newJob;

@Configuration
@ComponentScan(basePackages = "com.example.aerobankapp.scheduler")
public class QuartzConfig
{

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JobFactory jobFactory()
    {
        return
    }

    @Bean
    @Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SchedulerFactoryBean schedulerBean() throws SchedulerException
    {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory());
        return new SchedulerFactoryBean();
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Scheduler scheduler(@Qualifier("schedulerBean") SchedulerFactoryBean schedulerFactoryBean)
    {
        return schedulerFactoryBean.getScheduler();
    }


}

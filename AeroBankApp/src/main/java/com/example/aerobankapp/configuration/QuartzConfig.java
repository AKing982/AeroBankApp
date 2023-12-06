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
    @Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SchedulerFactoryBean scheduler() throws SchedulerException
    {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory());
        return schedulerFactoryBean;
    }

    @Bean
    public SpringBeanJobFactory jobFactory()
    {
        return new SpringBeanJobFactory();
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JobDetailFactoryBean depositJobDetailFactoryBean(@Qualifier("deposit")Deposit deposit, @Qualifier("beanString") String data)
    {
        // Create the JobDataMap
        JobDataMap depositJobDataMap = new JobDataMap();
        depositJobDataMap.put(data, deposit);

        // Create and set the JobDetailFactoryBean
        JobDetailFactoryBean depositJobDetailFactoryBean = new JobDetailFactoryBean();
        depositJobDetailFactoryBean.setJobDataMap(depositJobDataMap);
        return depositJobDetailFactoryBean;
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JobDetailFactoryBean withdrawJobDetailFactoryBean(@Qualifier("withdraw")Withdraw withdraw, @Qualifier("beanString") String data)
    {
        JobDataMap withdrawJobDataMap = new JobDataMap();
        withdrawJobDataMap.put(data, withdraw);

        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobDataMap(withdrawJobDataMap);
        return jobDetailFactoryBean;
    }



    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JobDetailFactoryBean jobDetailFactoryBean(Class<? extends org.quartz.Job> job, JobDataMap dataMap, String description)
    {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(job);
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setJobDataMap(dataMap);
        jobDetailFactoryBean.setDescription(description);
        jobDetailFactoryBean.afterPropertiesSet();
        return jobDetailFactoryBean;
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(@Qualifier("jobDetailFactoryBean") JobDetail jobDetail)
    {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(jobDetail);
        simpleTriggerFactoryBean.setStartDelay(0L);
        return simpleTriggerFactoryBean;
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CronTriggerFactoryBean cronTriggerFactoryBean(@Qualifier("jobDetailFactoryBean") JobDetail jobDetail, @Value("${dailycron}") String cronExpression)
    {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setCronExpression(cronExpression);
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        return cronTriggerFactoryBean;
    }



}

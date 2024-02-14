package com.example.aerobankapp.configuration;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.scheduler.CronExpressionBuilder;
import com.example.aerobankapp.scheduler.factory.SpringJobFactory;
import com.example.aerobankapp.scheduler.jobs.DepositJob;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

@Configuration
public class SchedulerConfig
{

    @Bean
    public SpringJobFactory springJobFactory()
    {
        return new SpringJobFactory();
    }


    @Bean(name="quartzDataSource")
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/QUARTZ_SCHEMA");
        dataSource.setUsername("root");
        dataSource.setPassword("Halflifer94!");
        return dataSource;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource());
        factory.setQuartzProperties(quartzProperties());
        factory.setJobFactory(springJobFactory());

        return factory;
    }


    public Scheduler createScheduler(Trigger trigger, JobDetail jobDetail) throws SchedulerException, IOException {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        return scheduler;
    }

    public Properties quartzProperties()
    {
        Properties quartzProperties = new Properties();
        quartzProperties.put("org.quartz.scheduler.instanceName", "quartzScheduler");
        quartzProperties.put("org.quartz.scheduler.instanceId", "AUTO");
        quartzProperties.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        quartzProperties.put("org.quartz.threadPool.threadCount", "10");
        quartzProperties.put("org.quartz.threadPool.threadPriority", "5");
        quartzProperties.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        quartzProperties.put("org.quartz.jobStore.dataSource", "quartzDataSource");
        quartzProperties.put("org.quartz.dataSource.quartzDataSource.URL", "jdbc:mysql://localhost:3306/QUARTZ_SCHEMA");
        quartzProperties.put("org.quartz.dataSource.quartzDataSource.user", "root");
        quartzProperties.put("org.quartz.dataSource.quartzDataSource.password", "Halflifer94!");
        quartzProperties.put("org.quartz.dataSource.quartzDataSource.driver", "com.mysql.cj.jdbc.Driver");
        return quartzProperties;
    }

    @Bean
    public Scheduler scheduler() throws IOException, SchedulerException {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        scheduler.start();
        return scheduler;
    }

    public SimpleTriggerFactoryBean simpleJobTrigger(@Qualifier("simpleJobDetail") JobDetail jobDetail)
    {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(jobDetail);
        trigger.setRepeatInterval(3600000); // Repeat every 1 hour
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    @Bean
    public JobDetailFactoryBean depositJobDetail(Deposit deposit)
    {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();

        Random random = new Random();
        int randomNumber = random.nextInt(Integer.MAX_VALUE) + 1;

        jobDetailFactory.setJobClass(DepositJob.class);
        jobDetailFactory.setName("DepositJob" + randomNumber);
        jobDetailFactory.setGroup("BankingJobs" + randomNumber);
        jobDetailFactory.setDurability(true);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("DepositData", deposit);
        jobDetailFactory.setJobDataMap(jobDataMap);

        return jobDetailFactory;
    }



    @Bean
    public JobDetailFactoryBean withdrawJobDetail()
    {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(DepositJob.class);
        jobDetailFactory.setName("WithdrawJob");
        jobDetailFactory.setGroup("BankingJobs");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

}

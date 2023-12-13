package com.example.aerobankapp.configuration;

import com.example.aerobankapp.scheduler.WithdrawScheduler;
import com.example.aerobankapp.scheduler.jobdetail.DepositJob;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import lombok.With;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.*;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import static org.quartz.JobBuilder.newJob;

@Configuration
@ComponentScan(basePackages = {"com.example.aerobankapp.scheduler", "com.example.aerobankapp.workbench.transactions"})
public class QuartzConfig
{

    @Autowired
    private Deposit deposit;

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JobFactory jobFactory()
    {
        return new SpringBeanJobFactory();
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

    /**
     * The following beans are to be used for the scheduler package classes
     * @return
     */

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Deposit deposit2(@Qualifier("deposit") Deposit deposit)
    {
        return deposit;
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Purchase purchase2(@Qualifier("purchase") Purchase purchase)
    {
        return purchase;
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Withdraw withdraw2(@Qualifier("withdraw") Withdraw withdraw)
    {
        return withdraw;
    }


}

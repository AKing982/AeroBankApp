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
@ComponentScan(basePackages = "com.example.aerobankapp.scheduler")
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

    @Bean
    public Deposit deposit2()
    {
        return new Deposit();
    }

    @Bean
    public Purchase purchase2()
    {
        return new Purchase();
    }

    @Bean
    public Withdraw withdraw2()
    {
        return new Withdraw();
    }


}

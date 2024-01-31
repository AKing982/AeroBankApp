package com.example.aerobankapp.configuration;


import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.quartz.*;

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

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JobDetailFactoryBean depositJobDetailFactoryBean()
    {
        return new JobDetailFactoryBean();
    }

    @Bean
    @Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JobDetailFactoryBean withdrawJobDetailFactoryBean()
    {
        return new JobDetailFactoryBean();
    }



    @Bean
    public Deposit deposit2(@Qualifier("deposit") Deposit deposit)
    {
        return deposit;
    }

    @Bean
    public Purchase purchase2(@Qualifier("purchase") Purchase purchase)
    {
        return purchase;
    }

    @Bean
    public Withdraw withdraw2(@Qualifier("withdraw") Withdraw withdraw)
    {
        return withdraw;
    }


}

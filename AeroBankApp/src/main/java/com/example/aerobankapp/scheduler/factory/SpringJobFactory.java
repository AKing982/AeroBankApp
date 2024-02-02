package com.example.aerobankapp.scheduler.factory;

import org.quartz.Job;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringJobFactory implements JobFactory, ApplicationContextAware {

    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    public Job newJob(final TriggerFiredBundle bundle, final org.quartz.Scheduler Scheduler) throws SchedulerException {
        return beanFactory.createBean(bundle.getJobDetail().getJobClass());
    }
}
package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.entity.TriggerCriteriaEntity;
import com.example.aerobankapp.services.TriggerCriteriaService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Deprecated
public class ScheduleRunner implements Runnable
{
    private final SchedulerEngine schedulerEngine;
    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleRunner.class);

    @Autowired
    public ScheduleRunner(SchedulerEngine schedulerEngine)
    {
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    public void run()
    {
        try
        {
            schedulerEngine.startScheduler();

        }catch(SchedulerException e)
        {

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void startScheduler()
    {
        try
        {
            schedulerEngine.startScheduler();

        }catch(SchedulerException | InterruptedException ex)
        {

        }
    }

    public void scheduleJob(JobDetail jobDetail, Trigger trigger)
    {
        try
        {
            schedulerEngine.scheduleJob(jobDetail, trigger);

        }catch(SchedulerException ex)
        {
            LOGGER.error("An exception has occurred scheduling a job: ", ex);
        }
    }

    @PostConstruct
    public void init()
    {
        run();
    }

    @PreDestroy
    public void cleanup() throws SchedulerException {
        schedulerEngine.stopScheduler();
    }
}

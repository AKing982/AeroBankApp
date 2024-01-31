package com.example.aerobankapp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class SchedulerEngineImpl implements SchedulerEngine
{
    private final CronExpressionBuilder cronExpressionBuilder;

    public SchedulerEngineImpl(CronExpressionBuilder cronExpressionBuilder)
    {
        this.cronExpressionBuilder = cronExpressionBuilder;
    }

    public String getCronExpression()
    {
        return cronExpressionBuilder.getCronExpression();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Scheduled(cron="getCronExpression")
    public void runMonthlyTask() {

    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void runWeeklyTask() {

    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void runDailyTask() {

    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void runTask() {

    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void runTaskBiWeekly() {

    }

    @Override
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void runTaskBiDaily() {

    }
}

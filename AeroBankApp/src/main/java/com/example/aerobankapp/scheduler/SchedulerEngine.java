package com.example.aerobankapp.scheduler;

public interface SchedulerEngine
{
    void runMonthlyTask();
    void runWeeklyTask();
    void runDailyTask();
    void runTask();
    void runTaskBiWeekly();
    void runTaskBiDaily();
}

package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class MonthlyCronBuilderFactory implements CronBuilderFactory {
    private StringBuilder cronStr = new StringBuilder();
    private List<String> monthlyCronExpression = new ArrayList<>();
    private TriggerCriteria triggerCriteria;
    private final AeroLogger aeroLogger = new AeroLogger(MonthlyCronBuilderFactory.class);

    @Autowired
    public MonthlyCronBuilderFactory(TriggerCriteria triggerCriteria) {
        this.triggerCriteria = triggerCriteria;
    }

    /**
     * To be used with methods not needing MonthlySchedules
     */
    public MonthlyCronBuilderFactory() {

    }

    public List<String> getMonthlyCronSchedules() {

        TriggerCriteria triggerCriteria1 = getTriggerCriteria();
        return createCron(triggerCriteria1);
    }

    @Override
    public List<String> createCron(final TriggerCriteria triggerCriteria) {
        List<String> cronSchedules = new ArrayList<>();
        if (triggerCriteria != null) {
            int interval = triggerCriteria.getInterval();
            int min = triggerCriteria.getMinute();
            int hour = triggerCriteria.getHour();
            int day = triggerCriteria.getDay();
            int month = triggerCriteria.getMonth();
            int year = triggerCriteria.getYear();

            cronSchedules = getMonthlyCronSchedules(interval, min, hour, day, month, year);
        } else {
            throw new NullPointerException("Null Trigger Criteria");
        }

        return cronSchedules;
    }

    private List<String> getMonthlyCronSchedules(int interval, int min, int hour, int day, int month, int year) {
        List<String> monthlyCronSchedules = new ArrayList<>();
        if (interval == 1) {
            String cronSchedule = String.format("0 %d %d %d %d ? %d", min, hour, day, month, year);
            aeroLogger.info("CronSchedule: " + cronSchedule);
            monthlyCronSchedules.add(cronSchedule);
        } else if (interval <= 0 || min <= 0 || hour <= 0 || day <= 0 || month <= 0 || year <= 0) {
            throw new IllegalArgumentException("Trigger Criteria is invalid");
        } else {
            monthlyCronSchedules = generateSchedule(interval, min, hour, day, month, year, monthlyCronSchedules);
        }
        return monthlyCronSchedules;
    }

    private List<String> generateSchedule(int interval, int min, int hour, int day, int month, int year, List<String> monthlyCronSchedules) {
        List<String> schedules = new ArrayList<>();
        for (int i = 0; i < 12; i += interval) {
            int currentMonth = (month + i - 1) % 12 + 1;

                // Get the cron schedule for the next year
                String cronExpression = getMonthlyCronScheduleAdjustmentByYearRoleOver(year, month, interval);
                aeroLogger.info("Current Month: " + currentMonth);
                aeroLogger.info("Cron expression: " + cronExpression);
                schedules.add(cronExpression);
        }
        return schedules;
    }


    public int getNumberOfIterationsFromCurrentYearToNthYear(int interval, int currentYear, int nthYear, int startMonth)
    {
        if(interval <= 0 || currentYear > nthYear || startMonth < 1 || startMonth > 12)
        {
            throw new IllegalArgumentException("Invalid Trigger Parameters");
        }
        int yearDiff = nthYear - currentYear;
        int totalMonths = yearDiff * 12 + (12 - startMonth + 1);
        int actualIterations = 0;
        for(int monthCount = 0; monthCount < totalMonths; monthCount += interval)
        {
            actualIterations++;
        }

        return actualIterations;
    }

    public List<String> generateScheduleForNthYear(int currentYear, int nthYear, int interval, int month, int day, int min, int hour)
    {
        List<String> generatedSchedules = new ArrayList<>();
        if(currentYear > 0 && nthYear > 0 && nthYear != currentYear)
        {
            int scheduleLength = nthYear - currentYear;
            aeroLogger.info("Schedule Length: " + scheduleLength);
            int iterations = getNumberOfIterationsFromCurrentYearToNthYear(interval, currentYear, nthYear, month);
            aeroLogger.info("Iterations: " + iterations);

            int iterationCount = 0;
            int previousMonth = 0;
            int currMonth = 0;
            for(int i = 0; iterationCount < iterations; i += interval)
            {
                previousMonth = currMonth;

                currMonth = (month + i - 1) % 12 + 1;
                aeroLogger.info("Computed Current Month: " + currMonth);
                aeroLogger.info("Previous Month: " + previousMonth);

                if(isResetToNewYear(previousMonth, currMonth))
                {
                    currentYear++;
                }
                // If the currentMonth exceeds a multiple of 12, increment the current year

                aeroLogger.info("Current Month: " + currMonth);
                String cronExpression = String.format("0 %d %d %d %d ? %d", min, hour, day, currMonth, currentYear);
                aeroLogger.info("Cron Expression: " + cronExpression);

                generatedSchedules.add(cronExpression);
                iterationCount++;
            }
        }

        return generatedSchedules;
    }


    public String getMonthlyCronScheduleByMonth(int month) {
        // Get the list of Monthly Cron Schedules
        List<String> monthlyCronSchedules = getMonthlyCronSchedules();
        String cronScheduleByMonth = "";

        // Validate the month
        if (month > 0 && month <= 12) {
            cronScheduleByMonth = monthlyCronSchedules.stream()
                    .filter(cronSchedule -> {
                        String[] parts = cronSchedule.split(" ");
                        return parts.length > 4 && Integer.parseInt(parts[4]) == month;
                    })
                    .findFirst()
                    .orElse(null);
        } else {
            throw new IllegalArgumentException("Invalid Month");
        }
        return cronScheduleByMonth;
    }

    public String getMonthlyCronScheduleAdjustmentByYearRoleOver(int year, int month, int interval) {
        List<String> monthlySchedules = getMonthlyCronSchedules();
        if (year > 0) {
            if (month > 0 && month <= 12) {
                if (!monthlySchedules.isEmpty()) {
                    for (String cronSchedule : monthlySchedules) {
                        String[] parts = cronSchedule.split(" ");
                        if (parts.length >= 7) {
                            int cronYear = Integer.parseInt(parts[6]);
                            int cronMonth = Integer.parseInt(parts[4]);
                            if (cronYear == year && cronMonth == month) {
                                // Increment the year
                                int yearIncremented = getYearIncrementWithCycle(year, month, interval);
                                parts[6] = String.valueOf(yearIncremented);
                                return String.join(" ", parts);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    @Deprecated
    public int getYearIncrement(int year) {
        return year + 1;
    }

    @Deprecated
    public int getYearIncrementWithCycle(int year, int previousMonth, int month) {
        int newYear = 0;
        boolean hasCycled = isResetToNewYear(previousMonth, month);
        if (hasCycled) {
            newYear = getYearIncrement(year);
        }
        return newYear;
    }
    public boolean isResetToNewYear(int prevMonth, int month) {
        if(prevMonth <= 12)
        {
            return month <= prevMonth;
        }
        return false;
    }

}

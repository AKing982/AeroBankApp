package com.example.aerobankapp.scheduler.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor(force = true)
public final class SchedulerCriteria
{
    private Long scheduleCriteriaID;
    private String scheduledTime;
    private String scheduledInterval;
    private LocalDate scheduledDate;
    private String scheduleType;
}

package com.example.aerobankapp.scheduler.criteria;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public final class SchedulerCriteria
{
    private Long scheduleCriteriaID;
    private String scheduledTime;
    private String scheduledInterval;
    private LocalDate scheduledDate;
    private String scheduleType;
}

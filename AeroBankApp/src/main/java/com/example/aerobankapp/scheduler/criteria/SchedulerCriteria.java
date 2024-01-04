package com.example.aerobankapp.scheduler.criteria;

import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public final class SchedulerCriteria {

    private Long scheduleCriteriaID;
    private String scheduledTime;
    private String scheduledInterval;
    private LocalDate scheduledDate;
    private String scheduleType;
    private boolean isCronScheduled;
    private int priority;
    private ScheduleRole scheduleRole;
    private List<Object> criteria;
}

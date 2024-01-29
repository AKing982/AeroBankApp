package com.example.aerobankapp.scheduler.criteria;

import com.example.aerobankapp.scheduler.ScheduleType;
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
    private int schedulerUserID;
    private String scheduledTime;
    private LocalDate scheduledDate;
    private ScheduleType scheduleType;
    private int priority;
    private ScheduleRole scheduleRole;
    private LocalDate createdAt;
    private String createdByUser;
    private List<Object> criteria;
}

package com.example.aerobankapp.scheduler.criteria;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private LocalTime scheduledTime;
    private LocalDate scheduledDate;
    private ScheduleType scheduleType;
    private int priority;
    private LocalDate createdAt;
    private String createdByUser;
}

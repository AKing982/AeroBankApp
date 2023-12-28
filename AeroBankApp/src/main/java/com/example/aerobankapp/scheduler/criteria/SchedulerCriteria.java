package com.example.aerobankapp.scheduler.criteria;

import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public record SchedulerCriteria(Long scheduleCriteriaID,
                                String scheduledTime,
                                String scheduledInterval,
                                LocalDate scheduleDate,
                                String scheduleType,
                                boolean isCronScheduled,
                                int priority,
                                ScheduleRole scheduleRole,
                                List<Object> criteria) {

}

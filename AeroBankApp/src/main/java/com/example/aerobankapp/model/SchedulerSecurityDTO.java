package com.example.aerobankapp.model;

import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Builder
public record SchedulerSecurityDTO(Long schedulerSecurityID,
                                   int userID,
                                   String userName,
                                   boolean isScheduleAllowed,
                                   boolean isTriggerEnabled,
                                   boolean isCronTriggerEnabled,
                                   boolean isAutoshutdown,
                                   boolean isAutoStartup,
                                   boolean isPauseEnabled,
                                   boolean isShutdownEnabled,
                                   boolean isStartEnabled,
                                   ScheduleRole scheduleRole,
                                   LocalDate dateModified) {


}

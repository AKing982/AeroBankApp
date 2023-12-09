package com.example.aerobankapp.model;

import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SchedulerSecurityDTO
{
    private Long schedulerSecurityID;
    private int userID;
    private String user;
    private boolean isScheduleAllowed;
    private boolean isTriggerEnabled;
    private boolean isCronTriggerEnabled;
    private boolean isAutoShutdown;
    private boolean isAutoStartup;
    private boolean isJobModifiable;
    private boolean hasJobCreateRights;
    private boolean isPausedEnabled;
    private boolean isShutdownEnabled;
    private boolean isStartEnabled;
    private boolean canUpdateTriggers;
    private ScheduleRole scheduleRole;
    private LocalDate dateModified;

}

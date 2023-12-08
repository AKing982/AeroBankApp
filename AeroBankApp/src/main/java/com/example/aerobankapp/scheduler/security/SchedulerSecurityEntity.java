package com.example.aerobankapp.scheduler.security;

import com.example.aerobankapp.workbench.utilities.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.util.Properties;

@Getter
@Setter
@Table(name="schedulerSecurity")
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerSecurityEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UserProfile userProfile;
    private boolean isSchedulerUser;
    private boolean isSchedulerAdmin;
    private boolean isScheduleAllowed;
    private boolean isCronScheduled;
    private Properties securityProperties;

}

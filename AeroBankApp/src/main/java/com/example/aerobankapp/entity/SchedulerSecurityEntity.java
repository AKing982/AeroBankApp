package com.example.aerobankapp.entity;

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
    private String user;
    private String schedulerRole;
    private boolean isScheduleAllowed;
    private boolean isCronScheduled;

}

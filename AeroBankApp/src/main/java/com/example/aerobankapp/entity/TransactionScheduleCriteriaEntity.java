package com.example.aerobankapp.entity;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleInterval;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="transactionScheduleCriteria")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionScheduleCriteriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionScheduleID;

    @Column(name="scheduledDate")
    @NotNull
    private LocalDate scheduledDate;

    @Column(name="scheduledTime")
    @NotNull
    private LocalTime scheduledTime;

    @Column(name="scheduledInterval")
    @Enumerated(EnumType.STRING)
    private ScheduleInterval scheduleInterval;

    @Column(name="isScheduledNow")
    private boolean isScheduledNow;

    @Column(name="useDefaultDateSchedule")
    private boolean useDefaultDateSchedule;

}

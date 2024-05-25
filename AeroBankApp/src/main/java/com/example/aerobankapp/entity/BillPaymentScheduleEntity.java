package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name="billPaymentSchedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentScheduleID;

    @Column(name="paymentDueDate")
    private LocalDate paymentDueDate;

    @Column(name="scheduledPaymentDate")
    private LocalDate scheduledPaymentDate;

    @Column(name="frequency")
    @Enumerated(EnumType.STRING)
    private ScheduleFrequency scheduleFrequency;

    @Column(name="scheduleStatus")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    @Column(name="isRecurring")
    private boolean isRecurring;

    @Column(name="autoPayEnabled")
    private boolean autoPayEnabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="paymentHistoryID")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private BillPaymentHistoryEntity billPaymentHistory;


}

package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.notifications.NotificationStatus;
import com.example.aerobankapp.workbench.utilities.notifications.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="billPaymentNotification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentNotificationEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billPaymentNotificationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="paymentScheduleID")
    @NotNull
    private BillPaymentScheduleEntity billPaymentSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    @NotNull
    private UserEntity user;

    @Column(name="notificationType")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name="notificationStatus")
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name="message")
    private String message;

    @Column(name="sentDate")
    @NotNull
    private LocalDate sentDate;




}

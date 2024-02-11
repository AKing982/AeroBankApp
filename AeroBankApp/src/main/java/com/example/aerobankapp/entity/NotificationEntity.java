package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Table(name="notifications")
public class NotificationEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity userEntity;

    @Column(name="message")
    private String message;

    @Column(name="sent")
    private LocalDateTime sent;

    @Column(name="hasBeenRead")
    private boolean hasBeenRead;

    @Column(name="priority")
    @Size(min=1, max=8)
    private int priority;

}

package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name="accountNotifications")
@Getter
@Setter
@AllArgsConstructor(access= AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AccountNotificationEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acctNotificationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID", referencedColumnName = "acctID")
    private AccountEntity account;

    @Column(name="message")
    @NotNull
    @Size(min=10, max=225)
    private String message;

    @Column(name="priority")
    private int priority;

}

package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.AccountNotificationCategory;
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
@Builder
public class AccountNotificationEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acctNotificationID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="acctID", referencedColumnName = "acctID")
    private AccountEntity account;

    @Column(name="title")
    private String title;

    @Column(name="message")
    @NotNull
    @Size(min=10, max=225)
    private String message;

    @Column(name="priority")
    private int priority;

    @Column(name="isRead")
    private boolean isRead;

    @Column(name="isSevere")
    private boolean isSevere;

    @Column(name="accountNotificationCategory")
    @Enumerated(EnumType.STRING)
    private AccountNotificationCategory accountNotificationCategory;
    public AccountNotificationEntity(AccountEntity account, String message, int priority){
        this.account = account;
        this.message = message;
        this.priority = priority;
    }

    public AccountNotificationEntity(AccountEntity account, String title, String message, int priority, boolean isRead, boolean isSevere, AccountNotificationCategory accountNotificationCategory) {
        this.account = account;
        this.title = title;
        this.message = message;
        this.priority = priority;
        this.isRead = isRead;
        this.isSevere = isSevere;
        this.accountNotificationCategory = accountNotificationCategory;
    }
}
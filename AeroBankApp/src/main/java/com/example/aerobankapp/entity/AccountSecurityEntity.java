package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="accountSecurity")
public class AccountSecurityEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int aSecID;

    @Column(name="userID")
    private int userID;

    @Column(name="acctID")
    @NotNull
    private String acctID;

    @Column(name="isAccountLocked")
    private boolean isAccountLocked;

    @Column(name="isWithdrawEnabled")
    private boolean isWithdrawEnabled;

    @Column(name="interestEnabled")
    private boolean interestEnabled;

    @Column(name="autoPay_enabled")
    private boolean autoPay_enabled;

    @Column(name="isLinked")
    private boolean isLinked;

    @Column(name="maxWithdrawals")
    private int maxWithdrawals;
}

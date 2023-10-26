package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="accountSecurity")
public class AccountSecurity
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

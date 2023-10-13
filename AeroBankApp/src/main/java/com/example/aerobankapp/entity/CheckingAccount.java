package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.model.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(name="checkingAccount")
public class CheckingAccount
{
    @Id
    private String id;

    @Column(name="uID")
    private int uID;

    @Column(name="accountName")
    private String accountName;

    @Column(name="userName")
    private String userName;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="fees")
    private BigDecimal fees;

    @Column(name="interestRate")
    private BigDecimal interestRate;

    @Column(name="minimum_balance")
    private BigDecimal minimumBalance;

    @Column(name="numWithdrawals")
    private int numberOfWithdrawals;

    @Column(name="isLinked")
    private boolean isLinked;

    public CheckingAccount(int uID, String accountName, String userName, BigDecimal balance, BigDecimal fees) {
        this.uID = uID;
        this.accountName = accountName;
        this.userName = userName;
        this.balance = balance;
        this.fees = fees;
    }
}

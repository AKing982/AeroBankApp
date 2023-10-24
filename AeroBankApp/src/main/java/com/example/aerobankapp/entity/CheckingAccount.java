package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.Account;
import com.example.aerobankapp.model.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="checkingAccount")
public class CheckingAccount
{
    @Id
    private String id;

    @Column(name="uID")
    private int uID;

    @Column(name="aSecID")
    private int aSecID;

    @Column(name="accountName")
    @NotNull
    private String accountName;

    @Column(name="userName")
    @NotNull
    private String userName;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="interestRate")
    private BigDecimal interestRate;

    @Column(name="minimum_balance")
    private BigDecimal minimumBalance;

}

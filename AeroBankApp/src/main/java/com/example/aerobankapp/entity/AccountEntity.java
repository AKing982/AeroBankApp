package com.example.aerobankapp.entity;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.embeddables.AccountUserEmbeddable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="account")
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccountEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int acctID;

    @Column(name="externalId")
    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="accountCodeID")
    @JsonIgnore
    private AccountCodeEntity accountCode;

    @ManyToOne
    @JoinColumn(name="userID")
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="aSecID")
    private AccountSecurityEntity accountSecurity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accountPropsID")
    private AccountPropertiesEntity accountProperties;

    @Column(name="accountName")
    private String accountName;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(name="interest")
    private BigDecimal interest;

    @Column(name="accountType")
    private String accountType;

    @Column(name="hasDividend")
    private boolean hasDividend;

    @Column(name="isRentAccount")
    private boolean isRentAccount;

    @Column(name="hasMortgage")
    private boolean hasMortgage;

    @Column(name="subtype")
    private String subtype;

    @ManyToMany(mappedBy = "accounts")
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy="accountEntity")
    @JsonBackReference
    private Set<TransactionStatementEntity> transactions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountSecurityEntity> securities;

    @OneToMany(mappedBy="account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountPropertiesEntity> accountPropertiesEntities;

    public AccountEntity(int acctID, String externalId, AccountCodeEntity accountCode, UserEntity user, AccountSecurityEntity accountSecurity, AccountPropertiesEntity accountProperties, String accountName, BigDecimal balance, BigDecimal interest, String accountType, boolean hasDividend, boolean isRentAccount, boolean hasMortgage, String subtype, Set<UserEntity> users, Set<TransactionStatementEntity> transactions, Set<AccountSecurityEntity> securities, Set<AccountPropertiesEntity> accountPropertiesEntities) {
        this.acctID = acctID;
        this.externalId = externalId;
        this.accountCode = accountCode;
        this.user = user;
        this.accountSecurity = accountSecurity;
        this.accountProperties = accountProperties;
        this.accountName = accountName;
        this.balance = balance;
        this.interest = interest;
        this.accountType = accountType;
        this.hasDividend = hasDividend;
        this.isRentAccount = isRentAccount;
        this.hasMortgage = hasMortgage;
        this.subtype = subtype;
        this.users = users;
        this.transactions = transactions;
        this.securities = securities;
        this.accountPropertiesEntities = accountPropertiesEntities;
    }

    public AccountEntity()
    {

    }

    public AccountEntity(int acctID, String accountType)
    {
        this.acctID = acctID;
        this.accountType = accountType;
    }

    public int getAcctID() {
        return acctID;
    }

    public void setAcctID(int acctID) {
        this.acctID = acctID;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public AccountCodeEntity getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(AccountCodeEntity accountCode) {
        this.accountCode = accountCode;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AccountSecurityEntity getAccountSecurity() {
        return accountSecurity;
    }

    public void setAccountSecurity(AccountSecurityEntity accountSecurity) {
        this.accountSecurity = accountSecurity;
    }

    public AccountPropertiesEntity getAccountProperties() {
        return accountProperties;
    }

    public void setAccountProperties(AccountPropertiesEntity accountProperties) {
        this.accountProperties = accountProperties;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isHasDividend() {
        return hasDividend;
    }

    public void setHasDividend(boolean hasDividend) {
        this.hasDividend = hasDividend;
    }

    public boolean isRentAccount() {
        return isRentAccount;
    }

    public void setRentAccount(boolean rentAccount) {
        isRentAccount = rentAccount;
    }

    public boolean isHasMortgage() {
        return hasMortgage;
    }

    public void setHasMortgage(boolean hasMortgage) {
        this.hasMortgage = hasMortgage;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public Set<TransactionStatementEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionStatementEntity> transactions) {
        this.transactions = transactions;
    }

    public Set<AccountSecurityEntity> getSecurities() {
        return securities;
    }

    public void setSecurities(Set<AccountSecurityEntity> securities) {
        this.securities = securities;
    }

    public Set<AccountPropertiesEntity> getAccountPropertiesEntities() {
        return accountPropertiesEntities;
    }

    public void setAccountPropertiesEntities(Set<AccountPropertiesEntity> accountPropertiesEntities) {
        this.accountPropertiesEntities = accountPropertiesEntities;
    }
}

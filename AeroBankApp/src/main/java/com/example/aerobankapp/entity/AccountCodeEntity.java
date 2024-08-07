package com.example.aerobankapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="accountCode")
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "account"})
public class AccountCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acctCodeID;

    @Column(name="first_initial_segment")
    private String first_initial_segment;

    @Column(name="last_initial_segment")
    private String last_initial_segment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID", referencedColumnName = "userID")
    private UserEntity user;

    @Column(name="accountType")
    private String accountType;

    @Column(name="year_segment")
    private int year_segment;

    @Column(name="account_segment")
    private int account_segment;

    public AccountCodeEntity(Long acctCodeID, String first_initial_segment, String last_initial_segment, UserEntity user, String accountType, int year_segment, int account_segment) {
        this.acctCodeID = acctCodeID;
        this.first_initial_segment = first_initial_segment;
        this.last_initial_segment = last_initial_segment;
        this.user = user;
        this.accountType = accountType;
        this.year_segment = year_segment;
        this.account_segment = account_segment;
    }

    public AccountCodeEntity() {

    }

}

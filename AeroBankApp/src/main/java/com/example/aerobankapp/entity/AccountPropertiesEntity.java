
package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="accountProperties")
@Entity
@Getter
@Setter
public class AccountPropertiesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountPropsID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="acctID")
    private AccountEntity account;

    @Column(name="acct_color")
    private String acct_color;

    @Column(name="image_url")
    private String image_url;

    @Override
    public String toString() {
        return "AccountPropertiesEntity{" +
                "accountPropsID=" + accountPropsID +
                ", account=" + account +
                ", acct_color='" + acct_color + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
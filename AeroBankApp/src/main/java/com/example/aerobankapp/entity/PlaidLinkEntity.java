package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="plaidLink")
@Data
@Builder
public class PlaidLinkEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;

    @Column(name="access_token")
    private String accessToken;

    @Column(name="item_id")
    private String item_id;

    @Column(name="institution_name")
    private String institution_name;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public PlaidLinkEntity(Long id, UserEntity user, String accessToken, String item_id, String institution_name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.accessToken = accessToken;
        this.item_id = item_id;
        this.institution_name = institution_name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PlaidLinkEntity()
    {

    }
}

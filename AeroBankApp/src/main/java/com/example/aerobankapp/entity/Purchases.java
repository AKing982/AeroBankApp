package com.example.aerobankapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchases
{

    @Id
    @GeneratedValue
    private Long id;


}

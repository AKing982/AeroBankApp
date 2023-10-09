package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="savingsSecurity")
public class SavingsSecurity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
}

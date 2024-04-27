package com.example.aerobankapp.model;

import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


public record TransactionHistory(String description,
                                 BigDecimal amount,
                                 LocalDate dateScheduled,
                                 LocalTime timeScheduled,
                                 TransactionStatus status)
{

}

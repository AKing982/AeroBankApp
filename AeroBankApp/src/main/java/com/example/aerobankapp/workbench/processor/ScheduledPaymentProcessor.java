package com.example.aerobankapp.workbench.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

public interface ScheduledPaymentProcessor<T>
{
    TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(T payment);
}

package com.example.aerobankapp.workbench.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.TreeMap;

public interface PaymentProcessor<T>
{
    TreeMap<LocalDate, BigDecimal> processPaymentAndScheduleNextPayment(T payment);
}

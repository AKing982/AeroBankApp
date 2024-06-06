package com.example.aerobankapp.workbench.processor;

import java.util.List;

public interface PaymentProcessor<T, R>
{
    List<R> processPayments(List<T> payments);
    R processSinglePayment(T payment);
}

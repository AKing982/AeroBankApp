package com.example.aerobankapp.workbench.verification;

public interface PaymentVerifier<T>
{
    boolean verify(T payment);
}

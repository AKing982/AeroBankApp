package com.example.aerobankapp.workbench.verification;

public interface PaymentVerifier<T>
{
    Boolean verify(T payment);
}

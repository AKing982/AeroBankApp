package com.example.aerobankapp.exceptions;

public class ScheduledPaymentNotFoundException extends RuntimeException {
    public ScheduledPaymentNotFoundException(String message) {
        super(message);
    }
}

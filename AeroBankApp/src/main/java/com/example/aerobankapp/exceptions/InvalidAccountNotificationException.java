package com.example.aerobankapp.exceptions;

public class InvalidAccountNotificationException extends IllegalArgumentException
{
    public InvalidAccountNotificationException(String s) {
        super(s);
    }
}

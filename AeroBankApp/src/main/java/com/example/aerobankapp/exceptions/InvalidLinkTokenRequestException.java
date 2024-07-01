package com.example.aerobankapp.exceptions;

public class InvalidLinkTokenRequestException extends RuntimeException {
    public InvalidLinkTokenRequestException(String message) {
        super(message);
    }
}

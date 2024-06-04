package com.example.aerobankapp.model;

import lombok.Data;

@Data
public class Notification
{
    private Account account;
    private String message;
}

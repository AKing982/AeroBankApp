package com.example.aerobankapp.model;

import lombok.Data;

@Data
public class Notification
{
    private String title;
    private String message;
    private int priority;
    private boolean isRead;
    private boolean isSevere;

    public Notification(String title, String message, int priority, boolean isRead, boolean isSevere) {
        this.title = title;
        this.message = message;
        this.priority = priority;
        this.isRead = isRead;
        this.isSevere = isSevere;
    }

    public Notification() {
    }
}

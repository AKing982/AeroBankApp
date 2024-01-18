package com.example.aerobankapp.workbench.utilities.notifications;

import java.time.LocalDateTime;

public abstract class Notifications
{
    protected String message;
    protected int userID;
    protected LocalDateTime timeStamp;
    protected NotificationType type;
    protected boolean hasBeenRead;
    protected int priority;

    public Notifications(String message, int userID, LocalDateTime time,
                         NotificationType type, boolean hasBeenRead, int priority)
    {
        this.message = message;
        this.userID = userID;
        this.timeStamp = time;
        this.type = type;
        this.hasBeenRead = hasBeenRead;
        this.priority = priority;
    }

    public abstract void sendNotification();

    public void markAsRead()
    {
        this.hasBeenRead = true;
    }
}

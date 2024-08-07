package com.example.aerobankapp.workbench.utilities.notifications;

public interface NotificationStrategy<T>
{
    StringBuilder buildMessage(T payment);
}

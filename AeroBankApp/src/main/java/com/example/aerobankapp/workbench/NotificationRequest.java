package com.example.aerobankapp.workbench;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor(access= AccessLevel.PUBLIC)
@NoArgsConstructor(access=AccessLevel.PUBLIC)
public class NotificationRequest implements Serializable
{
    private int accountID;
    private String message;
    private int priority;
}

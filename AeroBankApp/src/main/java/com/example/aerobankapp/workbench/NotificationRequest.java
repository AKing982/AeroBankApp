package com.example.aerobankapp.workbench;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor(access= AccessLevel.PUBLIC)
@NoArgsConstructor(access=AccessLevel.PUBLIC)
public class NotificationRequest implements Serializable
{
    private int accountID;
    private String title;
    private String message;
    private int priority;

    @JsonProperty("isRead")
    private boolean isRead;

    @JsonProperty("isSevere")
    private boolean isSevere;

    @JsonProperty("AccountNotificationCategory")
    private AccountNotificationCategory accountNotificationCategory;

}

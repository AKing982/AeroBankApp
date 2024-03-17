package com.example.aerobankapp.workbench;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserLogResponse implements Serializable {
    private int sessionID;
    private int userID;
    private String username;
    private String lastLogout;
    private String lastLogin;
    private int sessionDuration;
    private boolean loginSuccess;
    private int loginAttempts;
    private String sessionToken;
}

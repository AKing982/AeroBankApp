package com.example.aerobankapp.workbench.session;

import com.example.aerobankapp.workbench.utilities.User;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@RedisHash("Session")
public class Session implements Serializable
{
    @Id
    private String sessionID;
    private Integer userID;
    private LocalDateTime lastAccessed;
    private LocalDateTime expires;
    private Map<String, Object> attributes;
    private boolean isValid;
}

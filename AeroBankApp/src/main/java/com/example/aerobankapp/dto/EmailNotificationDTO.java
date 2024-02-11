package com.example.aerobankapp.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmailNotificationDTO(String message,
                                   String subject,
                                   String sender,
                                   String recipient,
                                   int userID,
                                   LocalDateTime sent,
                                   int priority) {
}

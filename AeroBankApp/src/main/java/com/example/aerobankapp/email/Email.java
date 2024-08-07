package com.example.aerobankapp.email;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email
{
    private int emailID;
    private String sender;
    private String senderEmail;
    private String toEmail;
    private LocalDateTime timeStamp;
    private String subject;
    private String body;
    private List<String> recipients;

}

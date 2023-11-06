package com.example.aerobankapp.model;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email
{
    private int userID;
    private String user;
    private String email;
    private String description;
    private String outgoingServer;
    private String port;
    private boolean isTLS;
    private boolean isSSL;


}

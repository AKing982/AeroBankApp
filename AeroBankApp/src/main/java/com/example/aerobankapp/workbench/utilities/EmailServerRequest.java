package com.example.aerobankapp.workbench.utilities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailServerRequest
{
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean isTLS;
}

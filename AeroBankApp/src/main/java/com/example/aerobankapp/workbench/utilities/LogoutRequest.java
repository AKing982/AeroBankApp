package com.example.aerobankapp.workbench.utilities;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class LogoutRequest implements Serializable
{
    private LocalDateTime time;

    public LogoutRequest(LocalDateTime lastLogout){
        this.time = lastLogout;
    }

    public LogoutRequest(){

    }
}

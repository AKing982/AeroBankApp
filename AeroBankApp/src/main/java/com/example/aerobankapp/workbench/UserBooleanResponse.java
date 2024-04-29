package com.example.aerobankapp.workbench;

import com.example.aerobankapp.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBooleanResponse
{
    private boolean exists;

    public UserBooleanResponse(boolean exists){
        this.exists = exists;
    }
}

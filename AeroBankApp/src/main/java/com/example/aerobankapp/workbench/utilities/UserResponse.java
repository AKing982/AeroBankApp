package com.example.aerobankapp.workbench.utilities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse
{
    private String firstName;
    private String lastName;

    public UserResponse(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

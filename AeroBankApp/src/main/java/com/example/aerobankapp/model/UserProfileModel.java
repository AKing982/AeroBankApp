package com.example.aerobankapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Data
@Component
public class UserProfileModel
{
    private String user;
    public UserProfileModel(String user)
    {
        this.user = user;
    }
}

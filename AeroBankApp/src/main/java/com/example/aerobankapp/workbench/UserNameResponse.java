package com.example.aerobankapp.workbench;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserNameResponse {
    private String username;

    public UserNameResponse(String username){
        this.username = username;
    }
}

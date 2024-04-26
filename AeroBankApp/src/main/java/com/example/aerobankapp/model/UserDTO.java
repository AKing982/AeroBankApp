package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AccountNumber;
import com.example.aerobankapp.workbench.utilities.Role;
import lombok.*;
import org.springframework.stereotype.Component;


@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
@Component
public class UserDTO
{
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String pinNumber;
    private boolean isAdmin;
    private Role role;

    public UserDTO(String user)
    {
        this.userName = user;
    }
}

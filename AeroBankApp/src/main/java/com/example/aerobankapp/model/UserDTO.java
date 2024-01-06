package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AccountNumber;
import lombok.*;
import org.springframework.stereotype.Component;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class UserDTO
{
    private int id;
    private String userName;
    private String email;
    private AccountNumber accountNumber;
    private String password;
    private String pinNumber;

    public UserDTO(String user)
    {
        this.userName = user;
    }
}

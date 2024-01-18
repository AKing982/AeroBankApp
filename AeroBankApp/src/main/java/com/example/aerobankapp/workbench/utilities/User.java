package com.example.aerobankapp.workbench.utilities;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User
{
    private int userID;
    private String userName;
    private String email;
    private String password;
    private String pinNumber;
    private Role role;

}

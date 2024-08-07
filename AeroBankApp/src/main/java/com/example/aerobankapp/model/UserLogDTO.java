package com.example.aerobankapp.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Builder
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class UserLogDTO
{
    private int id;
    private int userID;
    private String userName;
    private Date lastLogin;
    private List<UserLogDTO> history;
}

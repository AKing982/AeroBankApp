package com.example.aerobankapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public record UserLogDTO(int id,
                         int userID,
                         String userName,
                         Date lastLogin,
                         List<UserLogDTO> history)
{

}

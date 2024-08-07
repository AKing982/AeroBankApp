package com.example.aerobankapp.workbench.utilities.response;

import com.example.aerobankapp.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserServiceResponse
{
    private List<UserDTO> userDTOList;
    private String posted;

    public UserServiceResponse(List<UserDTO> userDTOList)
    {
        this.userDTOList = userDTOList;
    }

    public UserServiceResponse(String response)
    {
        this.posted = response;
    }
}

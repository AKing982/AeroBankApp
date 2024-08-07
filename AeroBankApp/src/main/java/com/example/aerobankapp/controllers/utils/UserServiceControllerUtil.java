package com.example.aerobankapp.controllers.utils;

import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserServiceControllerUtil
{
    public static List<UserDTO> getUserDTOList(List<UserEntity> userEntities)
    {
        List<UserDTO> userDTOList = new ArrayList<>();
        for(UserEntity userEntity : userEntities)
        {
            UserDTO userDTO = buildUserDTO(userEntity);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    public static UserDTO buildUserDTO(UserEntity userEntity)
    {
        return UserDTO.builder()
                .userID(userEntity.getUserID())
                .userName(userEntity.getUserCredentials().getUsername())
                .email(userEntity.getUserDetails().getEmail())
                .accountNumber(userEntity.getUserDetails().getAccountNumber())
                .pinNumber(userEntity.getUserSecurity().getPinNumber())
                .password(userEntity.getUserCredentials().getPassword())
                .isAdmin(userEntity.getUserSecurity().isAdmin())
                .role(userEntity.getUserSecurity().getRole())
                .isEnabled(userEntity.getUserSecurity().isEnabled())
                .build();
    }
}

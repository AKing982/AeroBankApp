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
                .userName(userEntity.getUsername())
                .email(userEntity.getEmail())
                .accountNumber(userEntity.getAccountNumber())
                .pinNumber(userEntity.getPinNumber())
                .password(userEntity.getPassword())
                .isAdmin(userEntity.isAdmin())
                .role(userEntity.getRole())
                .isEnabled(userEntity.isEnabled())
                .build();
    }
}

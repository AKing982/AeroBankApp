package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.workbench.utilities.User;

public interface UserService
{
    void encryptUserEntity(UserEntity userEntity);
    void registerUser(User user);

}

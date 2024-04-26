package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.UserDTO;

public interface AccountCodeCreator
{
    AccountCode generateAccountCode(UserDTO userDTO, AccountInfoDTO accountInfoDTO);
}

package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountInfoDTO;

import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;


public interface AccountCodeCreator
{
    AccountCode generateAccountCode(User userDTO, AccountInfoDTO accountInfoDTO);

    String formatAccountCode(AccountCode accountCode);
}

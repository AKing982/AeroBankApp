package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountInfoDTO;

import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;

import java.util.List;


public interface AccountCodeCreator
{
    AccountCode generateAccountCode(User userDTO, AccountInfoDTO accountInfoDTO);

    List<AccountCode> generateListOfAccountCodes(User user, List<AccountInfoDTO> accountInfoDTOS);

    String formatAccountCode(AccountCode accountCode);
}

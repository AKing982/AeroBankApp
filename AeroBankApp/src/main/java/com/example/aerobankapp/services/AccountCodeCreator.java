package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountInfoDTO;

import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserDTO;

import java.util.List;


public interface AccountCodeCreator
{
    AccountCode generateAccountCode(User userDTO, AccountInfoDTO accountInfoDTO);

    List<AccountCode> generateListOfAccountCodes(User user, List<AccountInfoDTO> accountInfoDTOS);

    String formatAccountCode(AccountCode accountCode);

    AccountCode createAccountCode(User user, Account accountEntity);

    AccountCodeEntity createAccountCodeEntityFromAccountAndUser(AccountEntity accountEntity, UserEntity userEntity);
}

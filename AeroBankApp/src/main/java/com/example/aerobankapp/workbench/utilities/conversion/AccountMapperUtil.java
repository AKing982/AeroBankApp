package com.example.aerobankapp.workbench.utilities.conversion;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.AccountEntity;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountMapperUtil {


    public static AccountDTO convertToDTO(AccountEntity accountEntity)
    {
        return AccountDTO.builder()
                .accountName(accountEntity.getAccountName())
                .accountCode(accountEntity.getAccountCode())
                .balance(accountEntity.getBalance())
                .interest(accountEntity.getInterest())
                .userID(accountEntity.getUserID())
                .isRentAccount(accountEntity.isRentAccount())
                .hasMortgage(accountEntity.isHasMortgage())
                .hasDividend(accountEntity.isHasDividend())
                .aSecID(accountEntity.getASecID())
                .accountType(AccountType.valueOf(accountEntity.getAccountType()))
              //  .users(accountEntity.getUsers().stream().map(UserMapperHelper::convertToDTO).collect(Collectors.toSet()))
                .build();
    }
}

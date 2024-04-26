package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AccountCodeCreatorImpl implements AccountCodeCreator
{
    private AccountService accountService;
    private UserService userService;

    @Autowired
    public AccountCodeCreatorImpl(AccountService accountService, UserService userService){
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public AccountCode generateAccountCode(UserDTO userDTO, AccountInfoDTO accountInfoDTO) {
        // Fetch the first initial of the first Name
        String firstInitial = userDTO.getFirstName().substring(1);

        // Fetch the last initial of the last name
        String lastInitial = userDTO.getLastName().substring(1);

        // Fetch the user ID from the existing user in the database
        List<UserEntity> userEntity = userService.findByUserName(userDTO.getUserName());
        if(userEntity.isEmpty()){
            throw new UserNotFoundException("No User found with username: " + userDTO.getUserName());
        }

        int userID = userEntity.get(0).getUserID();
        AccountType accountType = AccountType.valueOf(accountInfoDTO.accountType());

        int year = getYearFromCurrentDate();
        int twoDigitYear = getTwoDigitYearSegment(year);

        // Sequence number will be determined by the latest accountID in the database
        int sequenceNum = getSequenceNumber();

        // Return the account code
        return new AccountCode(firstInitial, lastInitial, userID, accountType ,twoDigitYear, sequenceNum);
    }

    public int getSequenceNumber(){
        int currentID = accountService.getLatestAccountID();
        return currentID++;
    }

    private int getYearFromCurrentDate(){
        LocalDate now = LocalDate.now();
        return now.getYear();
    }

    public int getTwoDigitYearSegment(int year){
        if(year < 0){
            throw new IllegalArgumentException("Invalid Year caught: " + year);
        }
        String twoDigitYear = "";
        String yearStr = String.valueOf(year);

        if(yearStr.length() == 4){
            twoDigitYear = yearStr.substring(2, 4);
        }
        return Integer.parseInt(twoDigitYear);
    }
}

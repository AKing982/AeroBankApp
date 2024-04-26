package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class AccountCodeCreatorImpl implements AccountCodeCreator
{
    private final AccountService accountService;
    private final UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(AccountCodeCreatorImpl.class);

    @Autowired
    public AccountCodeCreatorImpl(AccountService accountService, UserService userService){
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public AccountCode generateAccountCode(UserDTO userDTO, AccountInfoDTO accountInfoDTO) {
        // Fetch the first initial of the first Name
        String firstInitial = getStartingInitialByName(userDTO.getFirstName());

        // Fetch the last initial of the last name
        String lastInitial = getStartingInitialByName(userDTO.getLastName());

        UserEntity userEntity = getUserEntityByUserName(userDTO.getUserName().trim());

        int userID = getUserIDFromUserEntity(userEntity);
        AccountType accountType = getAccountTypeFromStringType(accountInfoDTO.accountType());

        int twoDigitYear = getTwoDigitYearSegment(getYearFromCurrentDate());

        // Sequence number will be determined by the latest accountID in the database
        int sequenceNum = getSequenceNumber();

        // Return the account code
        return new AccountCode(firstInitial, lastInitial, userID, accountType,twoDigitYear, sequenceNum);
    }

    @Override
    public String formatAccountCode(AccountCode accountCode) {
        StringBuilder stringBuilder = new StringBuilder();
        LOGGER.info("AccountCode: " + accountCode.toString());
        LOGGER.info("UserID: " + accountCode.getUserID());
        if (assertAccountCodeParametersNotNull(accountCode)) {
            if (accountCode.getUserID() < 10) {
                stringBuilder.append(accountCode.getFirstInitial());
                stringBuilder.append(accountCode.getLastInitial());
                stringBuilder.append("0");
                stringBuilder.append(accountCode.getUserID());
                stringBuilder.append("-");
                stringBuilder.append(accountCode.getAccountType().getCode());
                stringBuilder.append(accountCode.getYear());
                stringBuilder.append("00");
                stringBuilder.append(accountCode.getSequence());
            }
        }
        return stringBuilder.toString();
    }


    private boolean assertAccountCodeParametersNotNull(AccountCode accountCode){
        return !accountCode.getFirstInitial().isEmpty() || !accountCode.getLastInitial().isEmpty()
                || accountCode.getAccountType() != null || accountCode.getYear() != 0 || accountCode.getUserID() != 0
                || accountCode.getSequence() != 0;
    }

    private String getStartingInitialByName(String name){
        return name.substring(0, 1);
    }

    private AccountType getAccountTypeFromStringType(String type){
        return AccountType.valueOf(type);
    }

    private int getUserIDFromUserEntity(UserEntity user){
        return user.getUserID();
    }

    public UserEntity getUserEntityByUserName(String userName){
        Optional<UserEntity> userEntityOptional = userService.findByUser(userName);
        return userEntityOptional.orElseThrow(() -> new UserNotFoundException("User Not Found with userName: " + userName));
    }

    public int getSequenceNumber(){
        int currentID = accountService.getLatestAccountID();
        return ++currentID;
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

package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public AccountCode generateAccountCode(User user, AccountInfoDTO accountInfoDTO) {
        // Fetch the first initial of the first Name
        String firstInitial = getStartingInitialByName(user.getFirstName());

        // Fetch the last initial of the last name
        String lastInitial = getStartingInitialByName(user.getLastName());

        UserEntity userEntity = getUserEntityByUserName(user.getUsername().trim());

        int userID = getUserIDFromUserEntity(userEntity);
        LOGGER.info("Account Type: " + accountInfoDTO.accountType());
        AccountType accountType = getAccountTypeFromStringType(accountInfoDTO.accountType());

        int twoDigitYear = getTwoDigitYearSegment(getYearFromCurrentDate());

        // Sequence number will be determined by the latest accountID in the database
        int sequenceNum = getSequenceNumber();

        // Return the account code
        return getAccountCodeForm(firstInitial, lastInitial, userID, accountType, twoDigitYear, sequenceNum);
    }

    private AccountCode getAccountCodeForm(String first, String last, int userID, AccountType accountType, int year, int accountID){
        return new AccountCode(first, last, userID, accountType, year, accountID);
    }

    @Override
    public List<AccountCode> generateListOfAccountCodes(User user, List<AccountInfoDTO> accountInfoDTOS) {
       List<AccountCode> accountCodes = new ArrayList<>();
       for(AccountInfoDTO accountInfoDTO : accountInfoDTOS){
           if(accountInfoDTO != null){
               AccountCode accountCode = generateAccountCode(user, accountInfoDTO);
               accountCodes.add(accountCode);
           }
       }
       return accountCodes;
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

    public AccountCodeEntity createAccountCodeEntityFromAccountAndUser(AccountEntity accountEntity, UserEntity user){
        AccountCodeEntity accountCodeEntity = new AccountCodeEntity();
        accountCodeEntity.setAccountType(accountEntity.getAccountType());

        String firstInitial = getStartingInitialByName(user.getUserDetails().getFirstName());
        String lastInitial = getStartingInitialByName(user.getUserDetails().getLastName());
        int year = getTwoDigitYearSegment(getYearFromCurrentDate());
        accountCodeEntity.setAccount_segment(accountEntity.getAcctID());
        accountCodeEntity.setYear_segment(year);
        accountCodeEntity.setLast_initial_segment(lastInitial);
        accountCodeEntity.setFirst_initial_segment(firstInitial);
        accountCodeEntity.setUser(user);
        return accountCodeEntity;
    }

    public AccountCodeEntity createAccountCodeEntityFromModel(final AccountCode accountCode){
        AccountCodeEntity aceCodeEntity = new AccountCodeEntity();
        UserEntity user = userService.findById(accountCode.getUserID());

        aceCodeEntity.setFirst_initial_segment(accountCode.getFirstInitial());
        aceCodeEntity.setLast_initial_segment(accountCode.getLastInitial());
        aceCodeEntity.setAccountType(accountCode.getAccountType().getCode());
        aceCodeEntity.setAccount_segment(accountCode.getSequence());
        aceCodeEntity.setUser(user);
        aceCodeEntity.setYear_segment(accountCode.getYear());
        return aceCodeEntity;
    }

    @Override
    public AccountCode createAccountCode(final User user, final Account account) {
        String firstName = getStartingInitialByName(user.getFirstName());
        String lastName = getStartingInitialByName(user.getLastName());
        int userID = user.getUserID();
        AccountType accountType = getAccountTypeFromStringType(account.getAccountType().getCode());
        int year = getTwoDigitYearSegment(getYearFromCurrentDate());
        int acctID = getSequenceNumber();
        return getAccountCodeForm(firstName, lastName, userID, accountType, year, acctID);
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
        return AccountType.getInstance(type);
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

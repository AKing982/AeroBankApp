package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountDataManager extends AbstractDataManager
{
    private final AccountCodeCreator accountCodeCreator;

    @Autowired
    public AccountDataManager(UserService userService,
                              AccountService accountService,
                              AccountSecurityService accountSecurityService,
                              AccountPropertiesService accountPropertiesService,
                              AccountCodeService accountCodeService,
                              AccountUsersEntityService accountUsersEntityService,
                              UserLogService userLogService,
                              AccountCodeCreator accountCodeCreator) {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountCodeService, accountUsersEntityService, userLogService);
        this.accountCodeCreator = accountCodeCreator;
    }

    /**
     * This method will be used for creating the account code during the
     * registration process
     * @param user
     * @param accountInfoDTO
     * @return AccountCode
     */
    public AccountCode createAccountCode(User user, AccountInfoDTO accountInfoDTO){
        return null;
    }

    public AccountEntity buildAccountEntity(final AccountInfoDTO accountInfoDTO, AccountCodeEntity accountCode, UserEntity user){
        return null;
    }

    public List<AccountEntity> processAccountAndSecurity(List<AccountCodeEntity> accountCodeEntities, List<AccountInfoDTO> accountInfoDTOS){
        return null;
    }

    public List<String> getFilteredZeroParameterList(List<String> unfilteredAccountCodeSegments){
        return null;
    }

    public String filterZeroParameterString(String segment){
        return null;
    }

    public boolean createAccount(AccountDTO account){
        return false;
    }

    public boolean modifyAccount(AccountDTO account){
        return false;
    }

    public boolean deleteAccount(int acctID){
        return false;
    }
}

package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.manager.*;

import com.example.aerobankapp.services.UserServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter

public class UserProfile
{
    private User user;

    private UserProfileFacade userProfileFacade;

    public UserProfile(User user)
    {
        Objects.requireNonNull(user, "User Cannot be null");
        this.user = user;
    }

    public String getUserName()
    {
        return user.getUserName();
    }

    public String getEmail()
    {
        return user.getEmail();
    }

    public String getPinNumber()
    {
        return user.getPinNumber();
    }

    public String getAccountNumber()
    {
        UserServiceImpl userDAO = userProfileFacade.getUserProfileService().getUserManager();

        return userDAO.getAccountNumberByUserName(user.getUserName());
    }

    public void setUserProfileFacade(UserProfileFacade userProfileFacade)
    {
        this.userProfileFacade = userProfileFacade;
    }


    public BigDecimal getAccountBalance(String acctID)
    {
        AccountManager accountManager = userProfileFacade.getUserProfileService().getAccountManager();
        return accountManager.getBalanceFromAccount(acctID);
    }

    public String getAccountNumber(String user)
    {
        UserServiceImpl userDAO = userProfileFacade.getUserProfileService().getUserManager();
        return userDAO.getAccountNumberByUserName(user);
    }

    public int getTotalAccounts(String user)
    {
        return 0;
    }

}

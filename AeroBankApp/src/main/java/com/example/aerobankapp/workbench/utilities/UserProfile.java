package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.manager.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Component
public class UserProfile
{
    private User user;

    private UserProfileFacade userProfileFacade;

    public UserProfile(User user)
    {
        Objects.requireNonNull(user, "User Cannot be null");
        this.user = user;
    }

    @Autowired
    public void setUserProfileFacade(UserProfileFacade userProfileFacade)
    {
        this.userProfileFacade = userProfileFacade;
    }

    public String getUsername()
    {
        return user.getUserName();
    }

    public Set<AccountDTO> getAllAccounts()
    {
        return null;
    }

    public BigDecimal getAccountBalance(String acctID)
    {
        AccountManager accountManager = userProfileFacade.getUserProfileService().getAccountManager();
        return accountManager.getBalanceFromAccount(acctID);
    }
}

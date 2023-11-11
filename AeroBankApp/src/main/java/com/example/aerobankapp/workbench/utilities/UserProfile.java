package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.services.UserProfileService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserProfile extends AbstractUserProfile
{
    private UserProfileService userProfileService;

    public UserProfile(String name)
    {
        super(name);
    }

    public List<CheckingAccount> getUserCheckingAccounts()
    {
        String user = super.username;
        super.checkingAccounts = userProfileService.getAccountServiceBundle().getCheckingService().findByUserName(user);
        return checkingAccounts;
    }


}

package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.CheckingAccount;
import com.example.aerobankapp.model.UserProfileModel;
import com.example.aerobankapp.services.UserProfileService;
import jakarta.persistence.Access;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@Component
public class UserProfile extends AbstractUserProfile
{
    private UserProfileService userProfileService;

    @Autowired
    public UserProfile(String user, UserProfileService userService)
    {
        super(user);
        this.userProfileService = userService;
    }

    public List<CheckingAccount> getUserCheckingAccounts()
    {
        String user = super.username;
        super.checkingAccounts = userProfileService.getAccountServiceBundle().getCheckingService().findByUserName(user);
        return checkingAccounts;
    }


}

package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.UserLogServiceImpl;
import com.example.aerobankapp.services.UserProfileService;
import com.example.aerobankapp.services.UserServiceImpl;
import com.example.aerobankapp.workbench.history.BalanceHistory;
import com.example.aerobankapp.workbench.model.AccountNumber;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class UserProfile extends AbstractUserProfile
{
    private UserProfileService userProfileService;

    public UserProfile(String name)
    {
        super(name);
        this.userProfileService = new UserProfileService(name);
    }

}

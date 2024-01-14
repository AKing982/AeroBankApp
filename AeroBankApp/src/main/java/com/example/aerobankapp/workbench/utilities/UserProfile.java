package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.account.AccountNumber;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.history.BalanceHistory;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Component
public class UserProfile extends AbstractUserProfile
{
    private UserProfileFacade userProfileFacade;

    public UserProfile(String user)
    {
        super(user);
    }

    @Autowired
    public void setUserProfileFacade(UserProfileFacade profileFacade)
    {
        this.userProfileFacade = profileFacade;
    }

    @Override
    protected List<AccountNumber> getAllAccountNumbers() {
        return null;
    }

    @Override
    protected List<Withdraw> getAllWithdraws() {
        return null;
    }

    @Override
    protected List<Purchase> getAllPurchases() {
        return null;
    }

    @Override
    protected List<Deposit> getAllDeposits() {
        return null;
    }

    @Override
    protected List<AccountEntity> getAllAccounts() {
        String user = super.username;
        return null;

    }

    @Override
    protected List<CardDesignator> getAllUserCards() {
        return null;
    }

    @Override
    protected Map<Integer, List<ImageView>> getUserCardImages() {
        return null;
    }

    @Override
    protected Map<Integer, AccountSecurityEntity> getAccountSecurityDetails() {
        return null;
    }

    @Override
    protected Map<Integer, List<BalanceHistory>> getBalanceHistories() {
        return null;
    }

    @Override
    protected Map<Integer, List<FeesDTO>> getAccountFees() {
        return null;
    }


    public boolean isCurrentUser(String user)
    {
        return false;
    }
}

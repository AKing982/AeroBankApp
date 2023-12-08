package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.entity.SchedulerSecurityEntity;
import com.example.aerobankapp.services.UserProfileService;
import com.example.aerobankapp.workbench.history.BalanceHistory;
import com.example.aerobankapp.workbench.model.AccountNumber;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Component
public class UserProfile extends AbstractUserProfile
{
    @Autowired
    private UserProfileService userProfileService;
    private boolean isCurrentUser;
    private boolean isCurrentSession;
    private boolean isAuthenticated;
    private boolean isScheduleUser;
    private boolean isScheduleAdmin;

    @Autowired
    public UserProfile(@Qualifier("beanString")String user)
    {
        super(user);
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
    protected List<CheckingAccount> getAllCheckingAccounts() {
        return null;
    }

    @Override
    protected List<SavingsAccount> getAllSavingsAccounts() {
        return null;
    }

    @Override
    protected List<InvestmentAccount> getAllInvestmentAccounts()
    {
        return null;
    }

    @Override
    protected List<RentAccount> getAllRentAccounts()
    {
        return null;
    }

    @Override
    protected List<MortgageAccount> getAllMortgageAccounts()
    {
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
    protected Map<Integer, AccountSecurity> getAccountSecurityDetails() {
        return null;
    }

    @Override
    protected Map<Integer, List<BalanceHistory>> getBalanceHistories() {
        return null;
    }

    @Override
    protected Collection<SchedulerSecurityEntity> getSchedulerSecurities() {
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

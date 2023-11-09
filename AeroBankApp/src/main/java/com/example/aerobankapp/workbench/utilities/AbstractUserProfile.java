package com.example.aerobankapp.workbench.utilities;


import com.example.aerobankapp.entity.AccountSecurity;
import com.example.aerobankapp.entity.MortgageAccount;
import com.example.aerobankapp.entity.RentAccount;
import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.model.CheckingAccount;
import com.example.aerobankapp.model.InvestmentAccount;
import com.example.aerobankapp.model.SavingsAccount;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.workbench.history.BalanceHistory;
import com.example.aerobankapp.workbench.model.AccountNumber;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractUserProfile
{
    protected LoggedUser loggedUser;
    protected User user;
    protected boolean isCurrentUser;
    protected boolean isAuthenticated;

    protected String username;
    protected List<CheckingAccount> checkingAccounts;
    protected List<SavingsAccount> savingsAccounts;
    protected List<InvestmentAccount> investmentAccounts;
    protected List<RentAccount> rentAccounts;
    protected List<MortgageAccount> mortgageAccounts;
    protected List<CardDesignator> cards;
    protected Map<Integer, List<ImageView>> cardImagesMap;
    protected Map<Integer, AccountSecurity> accountSecurityMap;
    protected Map<Integer, List<BalanceHistory>> balanceHistoryMap;
    protected Map<Integer, List<FeesDTO>> accountFees;
    protected List<AccountNumber> accountNumbers;
    protected List<Withdraw> withdraws;
    protected List<Purchase> purchases;
    protected List<Deposit> deposits;

    public AbstractUserProfile(String user)
    {
        this.username = user;
    }

}

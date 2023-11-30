package com.example.aerobankapp.workbench.utilities;


import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.fees.FeesDTO;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.model.UserProfileModel;
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
public abstract class AbstractUserProfile implements Cloneable {
    protected LoggedUser loggedUser;
    protected User user;
    protected boolean isCurrentUser;
    protected boolean isCurrentSession;
    protected boolean isValidUser;
    protected boolean isAuthenticated;
    private UserProfileModel userProfileModel;

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

    public AbstractUserProfile(String user) {
        this.username = user;
    }

    protected abstract List<AccountNumber> getAllAccountNumbers();
    protected abstract List<Withdraw> getAllWithdraws();
    protected abstract List<Purchase> getAllPurchases();
    protected abstract List<Deposit> getAllDeposits();
    protected abstract List<CheckingAccount> getAllCheckingAccounts();
    protected abstract List<SavingsAccount> getAllSavingsAccounts();
    protected abstract List<InvestmentAccount> getAllInvestmentAccounts();
    protected abstract List<RentAccount> getAllRentAccounts();
    protected abstract List<MortgageAccount> getAllMortgageAccounts();
    protected abstract List<CardDesignator> getAllUserCards();
    protected abstract Map<Integer, List<ImageView>> getUserCardImages();
    protected abstract Map<Integer, AccountSecurity> getAccountSecurityDetails();
    protected abstract Map<Integer, List<BalanceHistory>> getBalanceHistories();
    protected abstract Map<Integer, List<FeesDTO>> getAccountFees();

    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}

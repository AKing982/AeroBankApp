package com.example.aerobankapp.workbench.utilities;


import com.example.aerobankapp.account.AccountNumber;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.fees.FeesDTO;

import com.example.aerobankapp.model.UserDTO;
import com.example.aerobankapp.model.UserProfileModel;
import com.example.aerobankapp.workbench.history.BalanceHistory;
import com.example.aerobankapp.workbench.transactions.CardDesignator;
import com.example.aerobankapp.workbench.transactions.Deposit;
import com.example.aerobankapp.workbench.transactions.Purchase;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractUserProfile implements Cloneable {
    protected LoggedUser loggedUser;
    protected UserDTO user;
    private UserProfileModel userProfileModel;
    private Role userRole;

    protected String username;
    private boolean isEnabledForUser;
    protected List<CheckingAccountEntity> checkingAccounts;
    protected List<SavingsAccountEntity> savingsAccounts;
    protected List<InvestmentAccountEntity> investmentAccounts;
    protected List<RentAccountEntity> rentAccounts;
    protected List<MortgageAccountEntity> mortgageAccounts;
    protected List<CardDesignator> cards;
    protected Map<Integer, List<ImageView>> cardImagesMap;
    protected Map<Integer, AccountSecurityEntity> accountSecurityMap;
    protected Map<Integer, List<BalanceHistory>> balanceHistoryMap;
    protected Map<Integer, List<FeesDTO>> accountFees;
    protected List<AccountNumber> accountNumbers = new ArrayList<>();
    protected List<Withdraw> withdraws;
    protected List<Purchase> purchases;
    protected List<Deposit> deposits;

    public AbstractUserProfile(String user)
    {
        this.username = user;
    }

    protected abstract List<AccountNumber> getAllAccountNumbers();
    protected abstract List<Withdraw> getAllWithdraws();
    protected abstract List<Purchase> getAllPurchases();
    protected abstract List<Deposit> getAllDeposits();
    protected abstract List<CheckingAccountEntity> getAllCheckingAccounts();
    protected abstract List<SavingsAccountEntity> getAllSavingsAccounts();
    protected abstract List<InvestmentAccountEntity> getAllInvestmentAccounts();
    protected abstract List<RentAccountEntity> getAllRentAccounts();
    protected abstract List<MortgageAccountEntity> getAllMortgageAccounts();
    protected abstract List<CardDesignator> getAllUserCards();
    protected abstract Map<Integer, List<ImageView>> getUserCardImages();
    protected abstract Map<Integer, AccountSecurityEntity> getAccountSecurityDetails();
    protected abstract Map<Integer, List<BalanceHistory>> getBalanceHistories();
    protected abstract Map<Integer, List<FeesDTO>> getAccountFees();

    protected void addAccountNumbers(AccountNumber accountNumber)
    {
        this.accountNumbers.add(accountNumber);
    }

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

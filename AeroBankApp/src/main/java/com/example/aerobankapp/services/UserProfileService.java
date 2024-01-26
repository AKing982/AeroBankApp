package com.example.aerobankapp.services;

import com.example.aerobankapp.manager.*;
import com.example.aerobankapp.model.BalanceHistoryManager;
import com.example.aerobankapp.workbench.security.authentication.UserSecurityProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class UserProfileService
{
    private final AccountManager accountManager;
    private final BalanceHistoryManager balanceHistoryModel;
    private final DepositManager depositManager;
    private final WithdrawManager withdrawManager;
    private final PurchaseManager purchaseManager;
    private final TransferManager transferManager;
    private final FeeManager feeManager;
    private final UserDAOImpl userManager;
    private final UserSecurityProfile userSecurityProfile;


    public UserProfileService(AccountManager accountManager,
                              BalanceHistoryManager balanceHistoryModel,
                              DepositManager depositManager,
                              WithdrawManager withdrawManager,
                              PurchaseManager purchaseManager,
                              TransferManager transactionManager,
                              FeeManager feeManager,
                              UserDAOImpl userManager,
                              UserSecurityProfile securityProfile)
    {
        this.accountManager = accountManager;
        this.balanceHistoryModel = balanceHistoryModel;
        this.depositManager = depositManager;
        this.withdrawManager = withdrawManager;
        this.purchaseManager = purchaseManager;
        this.transferManager = transactionManager;
        this.feeManager = feeManager;
        this.userManager = userManager;
        this.userSecurityProfile = securityProfile;
    }


}

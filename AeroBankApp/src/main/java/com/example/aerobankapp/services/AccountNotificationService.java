package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.workbench.AccountNotificationCategory;

import java.util.List;
import java.util.Optional;

public interface AccountNotificationService extends ServiceDAOModel<AccountNotificationEntity>
{
    List<AccountNotificationEntity> getAccountNotificationsByMessage(int acctID, String message);

    List<AccountNotificationEntity> getAccountNotificationsByPriority(int acctID, int priority);

    Optional<AccountNotificationEntity> getAccountNotificationByTitle(int acctID, String title);

    List<AccountNotificationEntity> getAccountNotificationsThatAreRead(int acctID);

    List<AccountNotificationEntity> getUnreadAccountNotifications(int acctID);

    List<AccountNotificationEntity> getAccountNotificationsByCategory(AccountNotificationCategory category);

    List<AccountNotificationEntity> getAccountNotificationsByAcctAndCategory(int acctID, AccountNotificationCategory category);
}

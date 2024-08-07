package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.repositories.AccountNotificationRepository;
import com.example.aerobankapp.workbench.AccountNotificationCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountNotificationServiceImpl implements AccountNotificationService
{
    private final AccountNotificationRepository accountNotificationRepository;

    @Autowired
    public AccountNotificationServiceImpl(AccountNotificationRepository accountNotificationRepository){
        this.accountNotificationRepository = accountNotificationRepository;
    }

    @Override
    public List<AccountNotificationEntity> findAll() {
        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationRepository.findAll();
        if(accountNotificationEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Found Empty Account Notifications.");
        }
        return accountNotificationEntities;
    }

    @Override
    public void save(AccountNotificationEntity obj) {
        if(obj == null){
            throw new NullAccountNotificationException("Unable to Save Null Notification.");
        }
        accountNotificationRepository.save(obj);
    }

    @Override
    @Transactional
    public void delete(AccountNotificationEntity obj) {
        if(obj == null){
            throw new NullAccountNotificationException("Unable to delete Null notification.");
        }
        accountNotificationRepository.delete(obj);
    }



    @Override
    public Optional<AccountNotificationEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<AccountNotificationEntity> findByUserName(String user) {
        // NOT IMPLEMENTED
        return null;
    }

    @Override
    public List<AccountNotificationEntity> getAccountNotificationsByMessage(int acctID, String message) {
        if(acctID < 0 || message.isEmpty()){
            throw new InvalidAccountNotificationException("Found AccountNotification with bad accountID and no Message.");
        }

        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationRepository.findAccountNotificationsByMessage(acctID, message);
        if(accountNotificationEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Found Empty list of Account Notifications for account ID: " + acctID);
        }
        return accountNotificationEntities;
    }

    @Override
    public List<AccountNotificationEntity> getAccountNotificationsByPriority(int acctID, int priority) {
        if(priority < 0 && acctID < 0){
            throw new InvalidAccountNotificationException("Invalid Account Notification parameters found.");
        }
        if(priority < 0){
            throw new InvalidPriorityLevelFoundException("Invalid Priority Level found.");
        }
        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationRepository.findAccountNotificationsByPriorityLevel(acctID, priority);
        if(accountNotificationEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to retrieve Account Notifications for accountID: " + acctID + " found no notifications.");
        }
        return accountNotificationEntities;
    }

    @Override
    public Optional<AccountNotificationEntity> getAccountNotificationByTitle(int acctID, String title) {
        if(acctID < 0){
            throw new InvalidAccountIDException("Caught Invalid Account ID.");
        }
        if(title.isEmpty()){
            throw new IllegalArgumentException("Unable to retrieve notification with empty title.");
        }
        return accountNotificationRepository.findAccountNotificationByTitle(acctID, title);
    }

    @Override
    public List<AccountNotificationEntity> getAccountNotificationsThatAreRead(int acctID) {
        if(acctID < 0){
            throw new InvalidAccountIDException("Invalid AccountID was caught: " + acctID);
        }
        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationRepository.findReadAccountNotifications(acctID);
        if(accountNotificationEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to find Account Notifications for account ID: " + acctID);
        }
        return accountNotificationEntities;
    }

    @Override
    public List<AccountNotificationEntity> getUnreadAccountNotifications(int acctID) {
        if(acctID < 0){
            throw new InvalidAccountIDException("Caught Invalid AccountID: " + acctID);
        }
        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationRepository.findUnreadAccountNotifications(acctID);
        return accountNotificationEntities;
    }

    @Override
    public List<AccountNotificationEntity> getAccountNotificationsByUserID(int userID) {
        return accountNotificationRepository.findAccountNotificationEntitiesByUserID(userID);
    }

    @Override
    public List<AccountNotificationEntity> getAccountNotificationsByCategory(AccountNotificationCategory category) {
        if(category == null){
            throw new NullAccountNotificationCategoryException("Caught Null Category.");
        }
        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationRepository.findAccountNotificationsByCategory(category);
        if(accountNotificationEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to find Account Notifications for category: " + category);
        }
        return accountNotificationEntities;
    }

    @Override
    public List<AccountNotificationEntity> getAccountNotificationsByAcctAndCategory(int acctID, AccountNotificationCategory category) {
        if(acctID < 0){
            throw new InvalidAccountIDException("Caught Invalid AccountID: " + acctID);
        }
        List<AccountNotificationEntity> accountNotificationEntities = accountNotificationRepository.findAccountNotificationsByAcctAndCategory(acctID, category);
        if(accountNotificationEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to find Account Notifications for accountID: " + acctID + " and category: " + category);
        }
        return accountNotificationEntities;
    }

    @Override
    @Transactional
    public void deleteAccountNotification(int acctID, Long notificationID) {
        accountNotificationRepository.deleteAccountNotification(acctID, notificationID);
    }

    @Override
    @Transactional
    public void updateAccountNotificationAsRead(int acctID, Long notificationID) {
        accountNotificationRepository.updateAccountNotificationAsRead(acctID, notificationID);
    }

    @Override
    @Transactional
    public void deleteAll(List<AccountNotificationEntity> accountNotificationEntities) {
        accountNotificationRepository.deleteAll(accountNotificationEntities);
    }
}

package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountCodeDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.AccountCodeNotFoundException;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.repositories.AccountCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountCodeServiceImpl implements AccountCodeService
{

    private final AccountCodeRepository accountCodeRepository;

    @Autowired
    public AccountCodeServiceImpl(AccountCodeRepository accountCodeRepository){
        this.accountCodeRepository = accountCodeRepository;
    }

    @Override
    public List<AccountCodeEntity> findAll() {
        return null;
    }

    @Override
    public void save(AccountCodeEntity obj) {

    }

    @Override
    public void saveAll(List<AccountCodeEntity> accountCodeEntities) {
        accountCodeRepository.saveAll(accountCodeEntities);
    }

    @Override
    public void delete(AccountCodeEntity obj) {
        accountCodeRepository.delete(obj);
    }

    @Override
    @Transactional
    public void deleteAll(List<AccountCodeEntity> accountCodeEntities) {
        accountCodeRepository.deleteAll(accountCodeEntities);
    }

    @Override
    public Optional<AccountCodeEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public AccountCodeEntity findByUserIdAndAcctSegment(int userId, int acctSegment) {
        Optional<AccountCodeEntity> accountCodeEntityOptional = accountCodeRepository.findAccountCodeEntityByUserIdAndAcctSegment(userId, acctSegment);
        return accountCodeEntityOptional.orElseThrow(() -> new AccountCodeNotFoundException("Account Code Not Found for userID: " + userId + " and acctID: " + acctSegment));
    }

    @Override
    public List<AccountCodeEntity> findByUserName(String user) {
        return null;
    }



    @Override
    public List<AccountCodeEntity> getAccountCodesListByUserID(int userID) {
        return accountCodeRepository.findAccountCodeEntitiesByUserID(userID);
    }

    @Override
    public AccountCodeEntity buildAccountCodeEntity(AccountCode accountCodeDTO, UserEntity user) {
        return getAccountCodeBuilder(accountCodeDTO, user);
    }

    @Override
    public List<AccountCodeEntity> getAccountCodeEntityList(List<AccountCode> accountCodes, UserEntity user) {
        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        for(AccountCode accountCode : accountCodes){
            if(accountCode != null){
                AccountCodeEntity accountCodeEntity = getAccountCodeBuilder(accountCode, user);
                if(accountCodeEntity != null){
                    accountCodeEntities.add(accountCodeEntity);
                }
            }

        }
        return accountCodeEntities;
    }

    private AccountCodeEntity getAccountCodeBuilder(AccountCode accountCode, UserEntity user){
        return AccountCodeEntity.builder()
                .accountType(accountCode.getAccountType().getCode())
                .first_initial_segment(accountCode.getFirstInitial())
                .last_initial_segment(accountCode.getLastInitial())
                .year_segment(accountCode.getYear())
                .account_segment(accountCode.getSequence())
                .user(user)
                .build();
    }

    @Override
    public String getAccountCodeAsString(AccountCode accountCode) {
        return null;
    }

    @Override
    public String getAccountCodeShortSegment(int account_segment) {
        return accountCodeRepository.getAccountCodeShortSegment(account_segment);
    }

    @Override
    public String getAccountCodeShortSegmentByUser(String user) {
        return null;
    }

    @Override
    public String getFirstInitialByAcctCodeID(Long id) {
        return null;
    }
}

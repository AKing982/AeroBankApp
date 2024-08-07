package com.example.aerobankapp.services;


import com.example.aerobankapp.dto.AccountCodeDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.model.User;

import java.util.List;
import java.util.Optional;

public interface AccountCodeService extends ServiceDAOModel<AccountCodeEntity>
{
    @Override
    List<AccountCodeEntity> findAll();

    @Override
    void save(AccountCodeEntity obj);

    void saveAll(List<AccountCodeEntity> accountCodeEntities);

    @Override
    void delete(AccountCodeEntity obj);

    void deleteAll(List<AccountCodeEntity> accountCodeEntities);

    @Override
    Optional<AccountCodeEntity> findAllById(Long id);

    AccountCodeEntity findByUserIdAndAcctSegment(int userId, int acctSegment);

    @Override
    List<AccountCodeEntity> findByUserName(String user);

    List<AccountCodeEntity> getAccountCodesListByUserID(int userID);

    AccountCodeEntity buildAccountCodeEntity(AccountCode accountCodeDTO, UserEntity user);

    List<AccountCodeEntity> getAccountCodeEntityList(List<AccountCode> accountCodes, UserEntity user);

    String getAccountCodeAsString(AccountCode accountCode);

    String getAccountCodeShortSegment(int account_segment);

    String getAccountCodeShortSegmentByUser(String user);

    String getFirstInitialByAcctCodeID(Long id);

}

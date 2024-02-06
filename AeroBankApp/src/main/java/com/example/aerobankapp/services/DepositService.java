package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DepositService extends ServiceDAOModel<DepositsEntity>
{
    @Override
    List<DepositsEntity> findAll();

    @Override
    void save(DepositsEntity obj);

    @Override
    void delete(DepositsEntity obj);

    @Override
    Optional<DepositsEntity> findAllById(Long id);

    @Override
    List<DepositsEntity> findByUserName(String user);

    List<DepositsEntity> getDepositsByUserNameDesc(String user);

    List<DepositsEntity> getDepositsByAcctID(int acctID);

    List<DepositsEntity> getListOfDepositsByUserIDASC(Long id);
    List<DepositsEntity> getListOfDepositsByUserID_DESC(Long id);

}

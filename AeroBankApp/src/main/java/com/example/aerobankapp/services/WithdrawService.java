package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WithdrawService extends ServiceDAOModel<WithdrawEntity>
{

    @Override
    List<WithdrawEntity> findAll();

    @Override
    void save(WithdrawEntity obj);

    @Override
    void delete(WithdrawEntity obj);

    @Override
    Optional<WithdrawEntity> findAllById(Long id);

    @Override
    List<WithdrawEntity> findByUserName(String user);

    List<WithdrawEntity> getListOfWithdrawalsByUserIDAsc(Long id);

    List<WithdrawEntity> getListOfWithdrawalsByUserIDDesc(Long id);

    Withdraw getWithdrawalByDescription(String description);

    BigDecimal getWithdrawalAmountById(Long id);

}

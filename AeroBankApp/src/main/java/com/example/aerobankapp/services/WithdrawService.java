package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.utilities.Status;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    List<WithdrawEntity> getListOfWithdrawalsByUserIDAsc(int userID);

    List<WithdrawEntity> getListOfWithdrawalsByUserIDDesc(int userID);

    Optional<WithdrawEntity> getWithdrawalByDescription(String description);

    BigDecimal getWithdrawalAmountById(Long id);

    List<WithdrawEntity> findByUserID(int userID);

    List<WithdrawEntity> findByAccountID(int acctID);

    List<WithdrawEntity> findByStatus(Status status);

    List<WithdrawEntity> findWithdrawBetweenDates(LocalDate startDate, LocalDate endDate);

    List<WithdrawEntity> findWithdrawalsByAccountIDAndStatus(int acctID, Status status);



}

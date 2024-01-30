package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.workbench.transactions.Withdraw;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class WithdrawServiceImpl implements WithdrawService
{

    @Override
    public List<WithdrawEntity> findAll() {
        return null;
    }

    @Override
    public void save(WithdrawEntity obj) {

    }

    @Override
    public void delete(WithdrawEntity obj) {

    }

    @Override
    public Optional<WithdrawEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<WithdrawEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<WithdrawEntity> getListOfWithdrawalsByUserIDAsc(Long id) {
        return null;
    }

    @Override
    public List<WithdrawEntity> getListOfWithdrawalsByUserIDDesc(Long id) {
        return null;
    }

    @Override
    public Withdraw getWithdrawalByDescription(String description) {
        return null;
    }

    @Override
    public BigDecimal getWithdrawalAmountById(Long id) {
        return null;
    }
}

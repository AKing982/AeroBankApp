package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.DepositsEntity;

import java.util.List;
import java.util.Optional;

public class DepositServiceImpl implements DepositService
{

    @Override
    public List<DepositsEntity> findAll() {
        return null;
    }

    @Override
    public void save(DepositsEntity obj) {

    }

    @Override
    public void delete(DepositsEntity obj) {

    }

    @Override
    public Optional<DepositsEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<DepositsEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getDepositsByUserNameDesc(String user) {
        return null;
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserIDASC(Long id) {
        return null;
    }

    @Override
    public List<DepositsEntity> getListOfDepositsByUserID_DESC(Long id) {
        return null;
    }
}

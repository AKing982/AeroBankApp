package com.example.aerobankapp.services;



import com.example.aerobankapp.entity.SavingsAccountEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface SavingsAccountDAO extends ServiceDAOModel<SavingsAccountEntity>
{
    @Override
    List<SavingsAccountEntity> findAll();

    @Override
    void save(SavingsAccountEntity obj);

    @Override
    void delete(SavingsAccountEntity obj);

    @Override
    Optional<SavingsAccountEntity> findAllById(Long id);
}

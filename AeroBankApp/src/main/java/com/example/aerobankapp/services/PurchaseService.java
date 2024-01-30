package com.example.aerobankapp.services;


import com.example.aerobankapp.entity.PurchaseEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface PurchaseService extends ServiceDAOModel<PurchaseEntity>
{
    @Override
    List<PurchaseEntity> findAll();

    @Override
    void save(PurchaseEntity obj);

    @Override
    void delete(PurchaseEntity obj);

    @Override
    Optional<PurchaseEntity> findAllById(Long id);

    @Override
    List<PurchaseEntity> findByUserName(String user);
}

package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TriggerCriteriaEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface TriggerCriteriaService extends ServiceDAOModel<TriggerCriteriaEntity>
{
    @Override
    List<TriggerCriteriaEntity> findAll();

    @Override
    void save(TriggerCriteriaEntity obj);

    @Override
    void delete(TriggerCriteriaEntity obj);

    @Override
    Optional<TriggerCriteriaEntity> findAllById(Long id);
}

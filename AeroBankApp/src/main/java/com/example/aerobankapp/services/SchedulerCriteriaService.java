package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.util.List;
import java.util.Optional;

public interface SchedulerCriteriaService extends ServiceDAOModel<SchedulerCriteriaEntity>
{
    @Override
    List<SchedulerCriteriaEntity> findByUserName(String user);

    @Override
    List<SchedulerCriteriaEntity> findAll();

    @Override
    void save(SchedulerCriteriaEntity obj);

    @Override
    void delete(SchedulerCriteriaEntity obj);

    @Override
    Optional<SchedulerCriteriaEntity> findAllById(Long id);
}

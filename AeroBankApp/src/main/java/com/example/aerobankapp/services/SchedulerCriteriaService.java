package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.Status;

import java.time.LocalDate;
import java.time.LocalTime;
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

    List<SchedulerCriteriaEntity> findByUserID(int userID);

    List<SchedulerCriteriaEntity> findById(int id);

    List<SchedulerCriteriaEntity> findByScheduleType(ScheduleType scheduleType);

    List<SchedulerCriteriaEntity> findByScheduledDate(LocalDate date);

    List<SchedulerCriteriaEntity> findByScheduledTime(LocalTime time);

    void updateSchedulerCriteriaStatusByID(Status status, int id);

    SchedulerCriteriaEntity convertDepositToSchedulerCriteriaEntity(DepositsEntity depositsEntity);

}

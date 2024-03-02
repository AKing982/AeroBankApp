package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.repositories.SchedulerCriteriaRepository;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@PreAuthorize("isAuthenticated()")
public class SchedulerCriteriaServiceImpl implements SchedulerCriteriaService {
    private final SchedulerCriteriaRepository schedulerCriteriaRepository;
    private final DepositService depositService;

    @Autowired
    public SchedulerCriteriaServiceImpl(SchedulerCriteriaRepository schedulerCriteriaRepository,
                                        DepositService depositService) {
        this.schedulerCriteriaRepository = schedulerCriteriaRepository;
        this.depositService = depositService;
    }

    @Override
    public List<SchedulerCriteriaEntity> findByUserName(String user) {
        // NOT USED
        return null;
    }

    @Override
    public List<SchedulerCriteriaEntity> findAll() {
        return schedulerCriteriaRepository.findAll();
    }

    @Override
    public void save(final SchedulerCriteriaEntity obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Unable to Save Null SchedulerCriteria...");
        }
        schedulerCriteriaRepository.save(obj);
    }

    @Override
    public void delete(final SchedulerCriteriaEntity obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Unable to delete Null SchedulerCriteria...");
        }
        schedulerCriteriaRepository.delete(obj);
    }

    @Override
    public Optional<SchedulerCriteriaEntity> findAllById(final Long id) {
        return Optional.empty();
    }

    @Override
    public List<SchedulerCriteriaEntity> findByUserID(final int userID) {
        return schedulerCriteriaRepository.findByUserID(userID);
    }

    @Override
    public List<SchedulerCriteriaEntity> findById(final int id) {
        return schedulerCriteriaRepository.findById(id);
    }

    @Override
    public List<SchedulerCriteriaEntity> findByScheduleType(final ScheduleType scheduleType) {
        return schedulerCriteriaRepository.findByScheduleType(scheduleType);
    }

    @Override
    public List<SchedulerCriteriaEntity> findByScheduledDate(final LocalDate date) {
        return schedulerCriteriaRepository.findByScheduledDate(date);
    }

    @Override
    public List<SchedulerCriteriaEntity> findByScheduledTime(final LocalTime time) {
        return schedulerCriteriaRepository.findByScheduledTime(time);
    }

    @Override
    public void updateSchedulerCriteriaStatusByID(final Status status, final int id) {
        schedulerCriteriaRepository.updateSchedulerCriteriaStatusByID(status, id);
    }

    public void saveEntityBatch(final List<SchedulerCriteriaEntity> schedulerCriteriaEntities)
    {
        schedulerCriteriaRepository.saveAll(schedulerCriteriaEntities);
    }

    public List<SchedulerCriteriaEntity> convertDepositListToSchedulerCriteriaEntities(final List<DepositsEntity> depositsEntities)
    {
        return depositsEntities.stream()
                .map(this::convertDepositToSchedulerCriteriaEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SchedulerCriteriaEntity convertDepositToSchedulerCriteriaEntity(final DepositsEntity depositsEntity)
    {
        SchedulerCriteriaEntity schedulerCriteria = new SchedulerCriteriaEntity();
        schedulerCriteria.setScheduledDate(depositsEntity.getScheduledDate());
        schedulerCriteria.setScheduledTime(depositsEntity.getScheduledTime());
        schedulerCriteria.setScheduleType(depositsEntity.getScheduleInterval());
        schedulerCriteria.setPriority(1);
        schedulerCriteria.setStatus(Status.ACTIVE);
        return schedulerCriteria;
    }

    public List<DepositsEntity> getDepositsByUserID(int userID)
    {
        return depositService.findByUserID(userID);
    }
}

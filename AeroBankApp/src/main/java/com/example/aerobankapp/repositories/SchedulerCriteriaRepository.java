package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SchedulerCriteriaRepository extends JpaRepository<SchedulerCriteriaEntity, Long>
{
    @Query("SELECT p FROM SchedulerCriteriaEntity p WHERE p.schedulerUserID = ?1")
    List<SchedulerCriteriaEntity> findByUserID(int userID);

    @Query("SELECT p FROM SchedulerCriteriaEntity p WHERE p.schedulerCriteriaID = ?1")
    List<SchedulerCriteriaEntity> findById(int id);

    @Query("SELECT p FROM SchedulerCriteriaEntity p WHERE p.scheduleType = ?1")
    List<SchedulerCriteriaEntity> findByScheduleType(ScheduleType scheduleType);

    @Query("SELECT p FROM SchedulerCriteriaEntity p WHERE p.scheduledDate = ?1")
    List<SchedulerCriteriaEntity> findByScheduledDate(LocalDate date);

    @Query("SELECT p FROM SchedulerCriteriaEntity p WHERE p.scheduledTime = ?1")
    List<SchedulerCriteriaEntity> findByScheduledTime(LocalTime time);

    @Query("UPDATE SchedulerCriteriaEntity p SET p.status=:status WHERE p.schedulerCriteriaID=:id")
    void updateSchedulerCriteriaStatusByID(@Param("status") Status status, @Param("id") int id);

}

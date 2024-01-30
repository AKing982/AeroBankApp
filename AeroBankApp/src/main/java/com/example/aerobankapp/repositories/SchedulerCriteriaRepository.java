package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerCriteriaRepository extends CrudRepository<SchedulerCriteriaEntity, Long>
{

}

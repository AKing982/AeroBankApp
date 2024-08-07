package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TriggerCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerCriteriaRepository extends JpaRepository<TriggerCriteriaEntity, Long>
{

}

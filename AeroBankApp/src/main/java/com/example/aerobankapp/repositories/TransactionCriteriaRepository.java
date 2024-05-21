package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransactionCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionCriteriaRepository extends JpaRepository<TransactionCriteriaEntity, Long>
{
    
}
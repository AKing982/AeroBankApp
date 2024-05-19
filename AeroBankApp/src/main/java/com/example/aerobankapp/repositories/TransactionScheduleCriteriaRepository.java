package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransactionScheduleCriteriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionScheduleCriteriaRepository extends JpaRepository<TransactionScheduleCriteriaEntity, Long>
{

}

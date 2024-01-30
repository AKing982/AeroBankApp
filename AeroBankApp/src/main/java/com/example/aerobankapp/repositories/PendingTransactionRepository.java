package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.PendingTransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingTransactionRepository extends CrudRepository<PendingTransactionEntity, Long>
{

}

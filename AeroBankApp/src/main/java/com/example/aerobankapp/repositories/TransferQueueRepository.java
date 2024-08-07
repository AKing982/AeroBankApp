package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransferQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferQueueRepository extends JpaRepository<TransferQueueEntity, Long>
{

}

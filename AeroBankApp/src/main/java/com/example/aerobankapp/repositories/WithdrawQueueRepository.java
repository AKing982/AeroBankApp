package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.WithdrawQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawQueueRepository extends JpaRepository<WithdrawQueueEntity, Long>
{

}

package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.UserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLogEntity, Long>
{

}

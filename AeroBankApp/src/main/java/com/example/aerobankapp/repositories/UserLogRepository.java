package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.UserLog;
import com.example.aerobankapp.model.UserLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLogModel, Long>
{

}

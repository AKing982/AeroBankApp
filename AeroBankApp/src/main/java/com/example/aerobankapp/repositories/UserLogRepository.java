package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.UserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLogRepository extends JpaRepository<UserLogEntity, Long>
{
    @Query("SELECT e FROM UserLogEntity e WHERE e.username=:user")
    List<UserLogEntity> findByUserName(@Param("user") String user);

    @Query("SELECT e.ipAddress FROM UserLogEntity e WHERE e.id=:id")
    String getIPAddressById(@Param("id") int id);

    @Query("SELECT e.sessionToken FROM UserLogEntity e WHERE e.id=:id")
    String getSessionToken(@Param("id") int id);


}

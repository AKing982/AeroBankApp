package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.UserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserLogRepository extends JpaRepository<UserLogEntity, Long>
{
    @Query("SELECT e FROM UserLogEntity e JOIN e.userEntity u WHERE u.username=:user")
    List<UserLogEntity> findByUserName(@Param("user") String user);

    @Query("SELECT e FROM UserLogEntity e WHERE e.isActive=:isActive AND e.userEntity.userID=:userID")
    Optional<UserLogEntity> findUserLogEntriesByActiveStateAndUserID(@Param("isActive") boolean isActive, @Param("userID") int userID);

    @Query("SELECT e FROM UserLogEntity e WHERE e.id=:id AND e.lastLogin=:lastLogin")
    List<UserLogEntity> getUserLogsByLastLogin(@Param("id") Long id, @Param("lastLogin")LocalDateTime time);

    @Query("UPDATE UserLogEntity e SET e.lastLogout=:logout WHERE e.id=:id")
    @Modifying(clearAutomatically = true)
    void updateLastLogout(@Param("logout") LocalDateTime time, @Param("id") Long id);

    @Modifying
    @Query("UPDATE UserLogEntity e SET e.lastLogin=:lastLogin WHERE e.id=:id")
    void updateLastLogin(@Param("lastLogin") LocalDateTime lastLogin, @Param("id") Long id);

    @Modifying
    @Query("UPDATE UserLogEntity e SET e.sessionDuration=:duration WHERE e.id=:id")
    void updateSessionDuration(@Param("duration") int duration, @Param("id") Long id);

    @Modifying
    @Query("UPDATE UserLogEntity e SET e.isActive=:isActive WHERE e.id=:id")
    void updateIsActiveState(@Param("isActive") boolean isActive, @Param("id") Long id);

    @Modifying
    @Query("UPDATE UserLogEntity e SET e.loginAttempts=:attempts WHERE e.id=:id")
    void updateLoginAttempts(@Param("attempts") int attempts, @Param("id") Long id);

    @Modifying
    @Query("UPDATE UserLogEntity e SET e.userEntity=:user WHERE e.id=:id")
    void updateUser(@Param("user")UserEntity userEntity, @Param("id") Long id);

    @Query("SELECT e.userEntity.userID FROM UserLogEntity e WHERE e.id=:id AND e.isActive=true")
    int getCurrentLoggedOnUser(@Param("id") Long id);

    @Query("SELECT e.isActive FROM UserLogEntity e WHERE e.userEntity.userID=:userID")
    boolean isUserCurrentlyLoggedIn(@Param("userID") int userID);

    @Query("SELECT e FROM UserLogEntity e WHERE e.loginAttempts=:attempts AND e.userEntity.userID=:userID")
    UserLogEntity getUserLogByLoginAttemptsAndUserID(@Param("attempts") int attempts, @Param("userID") int userID);

}

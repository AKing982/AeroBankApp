package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountUsersRepository extends JpaRepository<AccountUserEntity, Long>
{
    @Query("SELECT e FROM AccountUserEntity e WHERE e.user.userID=:userID")
    List<AccountUserEntity> findAccountUserEntitiesByUserID(@Param("userID") int userID);
}

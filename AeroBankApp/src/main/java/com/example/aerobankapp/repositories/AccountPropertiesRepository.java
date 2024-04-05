package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountPropertiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountPropertiesRepository extends JpaRepository<AccountPropertiesEntity, Long>
{
    @Query("SELECT ap FROM AccountPropertiesEntity ap JOIN ap.account a JOIN a.users u WHERE u.username LIKE %:userName%")
    List<AccountPropertiesEntity> findAccountPropertiesByUserName(@Param("userName") String userName);
}
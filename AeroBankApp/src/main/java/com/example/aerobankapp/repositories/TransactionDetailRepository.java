package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransactionDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetailEntity, Long>
{
    @Query("SELECT td FROM TransactionDetailEntity td WHERE td.userEntity.username=:username")
    List<TransactionDetailEntity> findByUsername(@Param("username") String username);



}

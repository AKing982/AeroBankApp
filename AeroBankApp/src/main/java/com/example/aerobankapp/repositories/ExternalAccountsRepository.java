package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.ExternalAccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalAccountsRepository extends JpaRepository<ExternalAccountsEntity, Long>
{
    @Query("SELECT e FROM ExternalAccountsEntity e WHERE e.externalAcctID=:id")
    Optional<ExternalAccountsEntity> findByExternalAcctID(String id);

    @Query("SELECT e FROM ExternalAccountsEntity e WHERE e.account.acctID =:id")
    Optional<ExternalAccountsEntity> findByAccountID(@Param("id") int id);
}

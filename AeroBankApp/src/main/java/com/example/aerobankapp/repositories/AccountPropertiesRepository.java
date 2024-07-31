package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountPropertiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountPropertiesRepository extends JpaRepository<AccountPropertiesEntity, Long>
{
    @Query("SELECT ap FROM AccountPropertiesEntity ap JOIN ap.account a JOIN a.users u WHERE u.userCredentials.username LIKE %:userName%")
    List<AccountPropertiesEntity> findAccountPropertiesByUserName(@Param("userName") String userName);

    @Query("SELECT ap FROM AccountEntity a JOIN a.accountProperties ap WHERE a.user.userID=:userID")
    List<AccountPropertiesEntity> findAccountPropertiesEntitiesByUserID(@Param("userID") int userID);

    @Query("SELECT ap FROM AccountPropertiesEntity ap WHERE ap.account.acctID =:id")
    Optional<AccountPropertiesEntity> findByAcctID(@Param("id") int acctID);
}

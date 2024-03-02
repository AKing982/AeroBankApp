package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.DepositsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<DepositsEntity, Long>
{
    @Query("SELECT d FROM DepositsEntity d JOIN d.user u WHERE u.username=:user")
    List<DepositsEntity> findDepositsByUserName(@Param("user") String user);

    @Query("SELECT d FROM DepositsEntity d JOIN d.user u WHERE u.userID=:userID")
    List<DepositsEntity> findDepositsByUserID(@Param("userID") int userID);

    @Query("SELECT d FROM DepositsEntity d JOIN d.user u WHERE u.username=:user ORDER BY d.posted DESC")
    List<DepositsEntity> getDepositsByUserNameDesc(@Param("user") String user);

    @Query("SELECT d FROM DepositsEntity d JOIN d.account a WHERE a.acctID=:acctID")
    List<DepositsEntity> getDepositsByAcctID(@Param("acctID") int acctID);

    @Query("SELECT d FROM DepositsEntity d JOIN d.user u WHERE u.userID=:id ORDER BY d.posted ASC")
    List<DepositsEntity> getListOfDepositsByUserIDASC(@Param("id") int userID);

    @Query("SELECT d FROM DepositsEntity d JOIN d.user u WHERE u.userID=:id ORDER BY d.posted DESC")
    List<DepositsEntity> getListOfDepositsByUserID_DESC(@Param("id") int userID);

}

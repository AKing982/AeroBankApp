package com.example.aerobankapp.repositories;


import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.workbench.utilities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    @Query("SELECT e FROM UserEntity e WHERE e.username=:user")
    List<UserEntity> findByUserName(@Param("user") String user);

    @Query("SELECT CONCAT(e.firstName, ' ', e.lastName) FROM UserEntity e WHERE e.userID=:userID")
    String findFullNameByUserID(@Param("userID") int userID);

    @Modifying
    @Query("UPDATE UserEntity u SET u.username=:username, u.email=:email, u.role=:role, u.password = :password, u.pinNumber =:pinNumber, u.firstName =:firstName, u.lastName =:lastName WHERE u.userID =:id")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    void updateUser(@Param("id") int id, @Param("username") String username, @Param("email") String email, @Param("role") Role role, @Param("password") String password, @Param("pinNumber") String pinNumber, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT e.role FROM UserEntity e WHERE e.username=:user")
    Role getUserRole(@Param("user") String username);

    @Query("SELECT e.accountNumber FROM UserEntity e WHERE e.username=:user")
    String getAccountNumberByUserName(@Param("user") String user);

    @Query("SELECT e.email FROM UserEntity e WHERE e.userID=:userID")
    String getEmailByID(@Param("userID") int userID);

    @Query("SELECT e.pinNumber FROM UserEntity e WHERE e.userID=:userID")
    String getPinNumberByUserID(@Param("userID") int userID);

    @Query("SELECT e.username FROM UserEntity e")
    List<String> getListOfUsers();

    @Query("SELECT e.userID FROM UserEntity e WHERE e.username=:user")
    int getIDByUserName(@Param("user") String user);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.username=:user")
    int userExists(@Param("user") String user);

}

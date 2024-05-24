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
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    @Query(value="SELECT EXISTS(SELECT 1 FROM AccountEntity e JOIN e.users u WHERE u.userDetails.accountNumber =:accountNum)")
    boolean doesAccountNumberExist(@Param("accountNum") String acctNum);

    @Query("SELECT u FROM UserEntity u WHERE u.userID=:id")
    Optional<UserEntity> findById(@Param("id") int id);


    @Query("SELECT e FROM UserEntity e WHERE e.userCredentials.username=:user")
    List<UserEntity> findByUserName(@Param("user") String user);

    @Query("SELECT e.userID FROM UserEntity e WHERE e.userDetails.accountNumber=:acctNum")
    int findUserIDByAccountNumber(@Param("acctNum") String acctNumber);

    @Query("SELECT CONCAT(e.userDetails.firstName, ' ', e.userDetails.lastName) FROM UserEntity e WHERE e.userID=:userID")
    String findFullNameByUserID(@Param("userID") int userID);

    @Modifying
    @Query("UPDATE UserEntity u SET u.userCredentials.username=:username, u.userDetails.email=:email, u.userSecurity.role=:role, u.userCredentials.password = :password, u.userSecurity.pinNumber =:pinNumber, u.userDetails.firstName =:firstName, u.userDetails.lastName =:lastName WHERE u.userID =:id")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    void updateUser(@Param("id") int id, @Param("username") String username, @Param("email") String email, @Param("role") Role role, @Param("password") String password, @Param("pinNumber") String pinNumber, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Modifying
    @Query("UPDATE UserEntity u SET u.userCredentials.password=:pass WHERE u.userCredentials.username=:user")
    void updateUserPassword(@Param("pass") String password, @Param("user") String username);

    @Query("SELECT e.userSecurity.role FROM UserEntity e WHERE e.userCredentials.username=:user")
    Role getUserRole(@Param("user") String username);

    @Query("SELECT e.userDetails.accountNumber FROM UserEntity e WHERE e.userCredentials.username=:user")
    String getAccountNumberByUserName(@Param("user") String user);

    @Query("SELECT e.userDetails.email FROM UserEntity e WHERE e.userID=:userID")
    String getEmailByID(@Param("userID") int userID);

    @Query("SELECT e.userSecurity.pinNumber FROM UserEntity e WHERE e.userID=:userID")
    String getPinNumberByUserID(@Param("userID") int userID);

    @Query("SELECT e.userCredentials.username FROM UserEntity e")
    List<String> getListOfUsers();

    @Query("SELECT e.userID FROM UserEntity e WHERE e.userCredentials.username=:user")
    int getIDByUserName(@Param("user") String user);

    @Query("SELECT e.userDetails.email FROM UserEntity e WHERE e.userCredentials.username=:user")
    String findEmailByUserName(@Param("user") String username);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.userCredentials.username=:user")
    int userExists(@Param("user") String user);

    @Query("SELECT u FROM UserEntity u WHERE u.userCredentials.username=:user")
    Optional<UserEntity> fetchByUser(@Param("user") String user);

    @Query("SELECT u.userCredentials.password FROM UserEntity u WHERE u.userCredentials.username=:user")
    String findUsersCurrentPassword(@Param("user") String user);

}

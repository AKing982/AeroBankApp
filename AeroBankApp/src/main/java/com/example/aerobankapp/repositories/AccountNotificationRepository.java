package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.workbench.AccountNotificationCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountNotificationRepository extends JpaRepository<AccountNotificationEntity, Long>
{
    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.message=:message")
    List<AccountNotificationEntity> findAccountNotificationsByMessage(@Param("acctID") int acctID, @Param("message") String message);

    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.priority=:priority")
    List<AccountNotificationEntity> findAccountNotificationsByPriorityLevel(@Param("acctID") int acctID, @Param("priority") int priority);

    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.title=:title")
    Optional<AccountNotificationEntity> findAccountNotificationByTitle(@Param("acctID") int acctID, @Param("title") String title);

    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.isRead=true")
    List<AccountNotificationEntity> findReadAccountNotifications(@Param("acctID") int acctID);

    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.isRead=false")
    List<AccountNotificationEntity> findUnreadAccountNotifications(@Param("acctID") int acctID);

    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.isRead=:isRead")
    List<AccountNotificationEntity> findAccountNotificationsByIsRead(@Param("acctID") int acctID, @Param("isRead") boolean isRead);

    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.accountNotificationCategory=:category")
    List<AccountNotificationEntity> findAccountNotificationsByCategory(@Param("category")AccountNotificationCategory category);

    @Query("SELECT an FROM AccountNotificationEntity an JOIN an.account a WHERE a.user.userID=:userID")
    List<AccountNotificationEntity> findAccountNotificationEntitiesByUserID(@Param("userID") int userID);

    @Query("SELECT e FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.accountNotificationCategory=:category")
    List<AccountNotificationEntity> findAccountNotificationsByAcctAndCategory(@Param("acctID") int acctID, @Param("category") AccountNotificationCategory category);

    @Query("DELETE FROM AccountNotificationEntity e WHERE e.account.acctID=:acctID AND e.acctNotificationID=:id")
    @Modifying
    void deleteAccountNotification(@Param("acctID") int acctID, @Param("id") Long notificationID);

    @Query("UPDATE AccountNotificationEntity e SET e.isRead=true WHERE e.account.acctID=:acctID AND e.acctNotificationID=:id")
    @Modifying
    void updateAccountNotificationAsRead(@Param("acctID") int acctID, @Param("id") Long notificationID);
}

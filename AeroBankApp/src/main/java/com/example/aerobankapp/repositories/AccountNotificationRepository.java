package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountNotificationRepository extends JpaRepository<AccountNotificationEntity, Long>
{

}

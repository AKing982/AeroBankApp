package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountUsersRepository extends JpaRepository<AccountUserEntity, Long>
{

}
